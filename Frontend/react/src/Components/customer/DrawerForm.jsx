/* eslint-disable react/prop-types */
import {
    Button,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    useDisclosure  } from '@chakra-ui/react'
import CreateCustomerForm from './CreateCustomerForm';

const AddIcon = ()=> "+";
const CloseIcon= ()=>"x";


const DrawerForm = ({fetchCustomers}) =>{

    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
        <Button
            leftIcon={<AddIcon/>}
            onClick={onOpen}
            colorScheme={"teal"}
            >
                Add customer
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Create new Customer</DrawerHeader>
  
            <DrawerBody>
              <CreateCustomerForm onSuccess={fetchCustomers}/>
            </DrawerBody>
  
            <DrawerFooter>
            <Button
            leftIcon={<CloseIcon/>}
            onClick={onClose}
            colorScheme={"teal"}
            >
                Close
              </Button>
            </DrawerFooter>
          </DrawerContent>
        </Drawer>
        </>
    )
}

export default DrawerForm;




