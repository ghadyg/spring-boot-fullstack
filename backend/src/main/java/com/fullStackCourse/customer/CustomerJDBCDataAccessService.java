package com.fullStackCourse.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                Select id,name,email,password,age,gender,profile_image_id
                from customer
                LIMIT 1000
                """;

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomer(Integer id) {
        var sql = "select * from customer where id = ?";
        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper,id);
        return customers.stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
         var sql = "insert into customer(name,email,password,age,gender) " +
                 "values (?,?,?,?,?)";

        int update = jdbcTemplate.update(sql, customer.getName()
                , customer.getEmail()
                , customer.getPassword()
                , customer.getAge()
                , customer.getGender().name());

        System.out.println("jdbcTemplate.update :"+ update);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {

//        var sql = "select * from customer where email = ? ";
//        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper,email);
//        return !customers.isEmpty();
        var sql = "select count(id) from customer where email=?";
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count!=null &&  count > 0;
    }

    @Override
    public void deleteCustomer(Integer id) {
        var sql = "delete from customer where id = ?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {

//        var sql = "select * from customer where id = ?";
//        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper,id);
//        return !customers.isEmpty();

        var sql = "select count(id) from customer where id=?";
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count!=null &&  count > 0;
    }

    @Override
    public void updateCustomer(Customer customer) {
            var sql = """
                    Update customer set
                    name = ? ,
                    email = ? ,
                    age = ? 
                    where id = ?
                    """;
            jdbcTemplate.update(sql,customer.getName()
            ,customer.getEmail(),customer.getAge(),customer.getId());
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        var sql = "select * from customer where email = ?";
        List<Customer> customers = jdbcTemplate.query(sql,customerRowMapper,email);
        return customers.stream().findFirst();
    }

    @Override
    public void updateCustomerProfileImageId(String profileId, Integer customerId) {
        var sql = """
                Update customer
                set profile_image_id = ?
                where id = ?
                """;
        jdbcTemplate.update(sql,profileId,customerId);
    }
}
