package com.fullStackCourse.customer;


import com.fullStackCourse.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<CustomerDTO> getCustomers()
    {
        return customerService.getAllCustomers();
    }
    @GetMapping(path = "{id}")
    public CustomerDTO getCustomer(@PathVariable("id") Integer id)
    {
        return customerService.getCustomer(id);
    }

    @GetMapping("/Page")
    public List<CustomerDTO> selectAPageOfCustomer(@RequestParam("pageSize") Integer pageSize, @RequestParam("offset") Integer offset)
    {
        return customerService.selectAPageOfCustomer(pageSize,offset);
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest)
    {
        customerService.addCustomer(customerRegistrationRequest);
        String token = jwtUtil.issueToken(customerRegistrationRequest.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,token).build();

    }

    @DeleteMapping(path = "{id}")
    public void deleteCustomer(@PathVariable("id") Integer id)
    {
        customerService.deleteCustomer(id);
    }

    @PutMapping(path = "{id}")
    public void updateCustomer(@PathVariable("id") Integer id,@RequestBody CustomerUpdateRequest customerUpdateRequest)
    {
        customerService.updateCustomer(id,customerUpdateRequest);
    }

    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadCustomerProfilePicture(@PathVariable("customerId") Integer id,
                                             @RequestParam("file")MultipartFile file){
            customerService.uploadCustomerImage(id,file);
    }

    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getCustomerProfilePicture(@PathVariable("customerId") Integer id){
        return customerService.getCustomerProfilePicture(id);
    }

}
