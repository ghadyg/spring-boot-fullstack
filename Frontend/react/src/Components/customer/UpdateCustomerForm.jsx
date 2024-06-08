/* eslint-disable react/jsx-no-undef */
/* eslint-disable react/prop-types */
import React, {useCallback,useState,useEffect} from 'react'
import {useDropzone} from 'react-dropzone'
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack, VStack,Image } from '@chakra-ui/react';
import { UpdateCustomer, getProfilePicture, uploadCustomerProfile } from '../../services/client';
import { errorNotification, successNotification } from '../../services/notification';

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
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


const MyDropzone = ({id,fetchCustomers,onClose})=> {
  const onDrop = useCallback(acceptedFiles => {
    const formdata = new FormData();
    formdata.append('file',acceptedFiles[0])
    
      uploadCustomerProfile(
        id,formdata
      ).then((res)=>
      {
        successNotification("success","Profile successfully updated");
        fetchCustomers();
        onClose();
      }).catch((err)=>{
        console.log(objectUrl)
        errorNotification("Error","Failed to upload Image");
      })
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <Box {...getRootProps()}
      width={'100%'}
      textAlign={'center'}
      border={'dashed'}
      borderColor={'gray.200'}
      borderRadius={'3xl'}
      p={6}
      rounded={'md'}
      
    >
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the files here ...</p> :
          <p>Drag 'n' drop some files here, or click to select files</p>
      }
    </Box>
  )
}

// And now we can use these
const UpdateCustomerForm = ({fetchCustomers,initialValues,customerId,onClose}) => {
  
  return (
    <>
      <VStack spacing={'5'} mb={'5'}>
          <Image
            borderRadius={'full'}
            boxSize={'150px'}
            objectFit={'cover'}
            src={getProfilePicture(customerId)}
          />
          <MyDropzone id={customerId} fetchCustomers={fetchCustomers} onClose={onClose} />
      </VStack>

      <Formik
        initialValues={initialValues}
        validationSchema={Yup.object({
            name: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
            age: Yup.number()
            .min(15,"must be at least 15")
            .max(100,"must be less than 100")
            .required('Required'),
        })}
        onSubmit={(customer, { setSubmitting }) => {
          setSubmitting(true)
          UpdateCustomer(customer,customerId)
            .then(res=>{
            successNotification(
              "Customer saved",
              `${customer.name} was saved`,
            )
            fetchCustomers();
            onClose();
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
       {({isValid,isSubmitting,dirty })=>(
        <Form>
        <Stack spacing={"24px"}>
        <MyTextInput
          label="Name"
          name="name"
          type="text"
          placeholder="Jane"
        />

        <MyTextInput
          label="Email Address"
          name="email"
          type="email"
          placeholder="jane@formik.com"
        />

      <MyTextInput
          label="Age"
          name="age"
          type="number"
          placeholder="20"
        />


        <Button isDisabled={!(isValid && dirty) || isSubmitting} type="submit">Update</Button>
        </Stack>
      </Form>
       )
        
       }
      </Formik>
    </>
  );
};
export default UpdateCustomerForm;