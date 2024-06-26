/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import React from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack } from '@chakra-ui/react';
import { px } from 'framer-motion';
import { saveCustomer } from '../../services/client';
import { errorNotification, successNotification } from '../../services/notification';

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input"  {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
            <AlertIcon/>
            {meta.error}
            </Alert>
      ) : null}
    </Box>
  );
};



const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Select {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
        <AlertIcon/>
        {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

// And now we can use these
const CreateCustomerForm = ({onSuccess}) => {
  return (
    <>
      <Formik
        initialValues={{
          name: '',
          email: '',
          password:'',
          age: 0, // added for our checkbox
          gender: '', // added for our select
        }}
        validationSchema={Yup.object({
            name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
            password:Yup.string().min(6,"must be at least 6 characters").required("password cannot be empty!"),
            age: Yup.number()
            .min(15,"must be at least 15")
            .max(100,"must be less than 100")
            .required('Required'),
          gender: Yup.string()
            .oneOf(
              ['male', 'female'],
              'Invalid gender'
            )
            .required('Required'),
        })}
        onSubmit={(customer, { setSubmitting }) => {
          setSubmitting(true)
          saveCustomer(customer)
            .then(res=>{
            successNotification(
              "Customer saved",
              `${customer.name} was saved`,
            )
            onSuccess(res.headers["authorization"]);
          }).catch(err=>{
            
            errorNotification(
              err.code,
              err.response.data.message
            )
          }).finally(()=>{
            setSubmitting(false);
          })
        }}
      >
       {({isValid,isSubmitting })=>(
        <Form>
        <Stack spacing={"24px"}>
        <MyTextInput
          autoComplete='new-password'
          label="Name"
          name="name"
          type="text"
          placeholder="Jane"
        />

        <MyTextInput
          autoComplete='new-password'
          label="Email Address"
          name="email"
          type="email"
          placeholder="jane@formik.com"
        />

        <MyTextInput
          autoComplete='new-password'
          label="Password"
          name="password"
          type="password"
          placeholder={"Please enter a password"}
        />

      <MyTextInput
          autoComplete='new-password'
          label="Age"
          name="age"
          type="number"
          placeholder="20"
        />

        <MySelect label="Gender" name="gender">
          <option value="">Select a gender</option>
          <option value="male">Male</option>
          <option value="female">Female</option>
        </MySelect>

        <Button isDisabled={!isValid || isSubmitting} type="submit">Submit</Button>
        </Stack>
      </Form>
       )
        
       }
      </Formik>
    </>
  );
};
export default CreateCustomerForm;