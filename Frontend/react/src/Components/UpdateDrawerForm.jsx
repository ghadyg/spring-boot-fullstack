import {
    Button,
    Drawer,
    DrawerBody,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    DrawerContent,
    DrawerCloseButton,
    useDisclosure,
    Input
  } from '@chakra-ui/react'
import UpdateCustomerForm from './UpdateCustomerForm';

const AddIcon = ()=> "+";
const CloseIcon= ()=>"x";


const UpdateDrawerForm = ({fetchCustomers,initialValues,customerId}) =>{

    const { isOpen, onOpen, onClose } = useDisclosure()
    return(
        <>
        <Button
            
            onClick={onOpen}
            colorScheme={"teal"}
            >
                Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
          <DrawerOverlay />
          <DrawerContent>
            <DrawerCloseButton />
            <DrawerHeader>Update Customer</DrawerHeader>
  
            <DrawerBody>
              <UpdateCustomerForm fetchCustomers={fetchCustomers}
              initialValues={initialValues}
              customerId={customerId}/>
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

export default UpdateDrawerForm;




