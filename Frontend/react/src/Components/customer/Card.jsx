import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Button,
    useColorModeValue,
    Tag,
    AlertDialog,
  AlertDialogBody,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogContent,
  AlertDialogOverlay,
  AlertDialogCloseButton,
  useDisclosure
  } from '@chakra-ui/react';
import { useRef } from 'react';
import {deleteCustomer} from '../../services/client'
import {successNotification,errorNotification} from '../../services/notification'
import UpdateDrawerForm from './UpdateDrawerForm';
  
  
  export default function CardWithImage({id,name,email,age,gender,fetchCustomers}) {

    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = useRef()
    return (
      <Center py={6}>
        <Box
          maxW={'300px'}
          minW={'300px'}
          w={'full'}
          bg={useColorModeValue('white', 'gray.800')}
          boxShadow={'lg'}
          rounded={'md'}
          overflow={'hidden'}>
          <Image
            h={'120px'}
            w={'full'}
            src={
              gender ==="male" ? `https://randomuser.me/api/portraits/med/men/${id}.jpg` : `https://randomuser.me/api/portraits/med/women/${id%100}.jpg`
            }
            objectFit={'cover'}
          />
          <Flex justify={'center'} mt={-12}>
            <Avatar
              size={'xl'}
              src={
                gender ==="male" ? `https://randomuser.me/api/portraits/med/men/${id}.jpg` : `https://randomuser.me/api/portraits/med/women/${id%100}.jpg` 
              }
              alt={'Author'}
              css={{
                border: '2px solid white',
              }}
            />
          </Flex>
  
          <Box p={6}>
            <Stack spacing={2} align={'center'} mb={5}>
            <Tag>{id}</Tag>
              <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                {name}
              </Heading>
              <Text color={'black.500'}>{email}</Text>
              <Text color={'gray.500'}>{age}</Text>
              <Text color={'gray.500'}>{gender}</Text>
            </Stack> 
            <Stack direction={"row"} justify={"center"} spacing={1}>
              <Stack>
                <UpdateDrawerForm 
                fetchCustomers={fetchCustomers}
                initialValues={{name,email,age}}
                customerId={id}/>
              </Stack>
            <Stack>
            <Button colorScheme='red' onClick={onOpen}>
              Delete
            </Button>
              <AlertDialog
                  isOpen={isOpen}
                  leastDestructiveRef={cancelRef}
                  onClose={onClose}
                >
                  <AlertDialogOverlay>
                    <AlertDialogContent>
                      <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                        Delete Customer
                      </AlertDialogHeader>

                      <AlertDialogBody>
                        Are you sure? You can't undo this action afterwards.
                      </AlertDialogBody>

                      <AlertDialogFooter>
                        <Button ref={cancelRef} onClick={onClose}>
                          Cancel
                        </Button>
                        <Button colorScheme='red' 
                                onClick={()=>{
                                  deleteCustomer(id).then(res=>{
                                    successNotification(
                                      'Customer Deleted',
                                      `Customer ${name} was deleted`
                                    )
                                    fetchCustomers();  
                                  }
                                  ).catch(err=>{
                                    errorNotification(
                                      err.code,
                                      err.response.data.message
                                    )
                                  }).finally(()=>{
                                    onClose()
                                    
                                  })
                                  onClose;
                                }} 
                                ml={3}>
                          Delete
                        </Button>
                      </AlertDialogFooter>
                    </AlertDialogContent>
                  </AlertDialogOverlay>
              </AlertDialog>
            </Stack>
            </Stack>

          </Box>
        </Box>
      </Center>
    );
  }