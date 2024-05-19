import {Drawer, DrawerFooter, Spinner,Text,Wrap, WrapItem } from '@chakra-ui/react'
import SidebarWithHeader from "./Components/shared/SideBar.jsx"
import {useEffect,useState} from 'react'
import {getCustomers} from "./services/client.js"
import CardWithImage from "./Components/customer/Card.jsx"
import DrawerForm from "./Components/customer/DrawerForm.jsx"
import { errorNotification } from "./services/notification.js"



function Customer() {

  const [customers,setCustomer] = useState([]);
  const [loading,setLoading] = useState(false);
  const [err,setError] = useState("")

  const fetchCustomers =()=>{
    getCustomers().then(res=>{
      setCustomer(res.data)
    }).catch(err=>{
      setError(err.response.data.message)
      errorNotification(
        err.code,
        err.response.data.message
      )
    })
    .finally(()=>{
      setLoading(false)
    }
  )
  }
  useEffect(()=>{
    setLoading(true);
    fetchCustomers();
    
  },[])

  if(loading){
    return(
      <SidebarWithHeader>
              <Spinner
        thickness='4px'
        speed='0.65s'
        emptyColor='gray.200'
        color='blue.500'
        size='xl'
      />
      </SidebarWithHeader>
    )
  }

  if(err)
  {
      return(
        <SidebarWithHeader>
          <DrawerForm fetchCustomers={fetchCustomers} />
          <Text mt={5}>Ooops there was an error</Text>
        </SidebarWithHeader>
      )
  }
  if(customers.length<=0)
    {
      return(
        <SidebarWithHeader>
          <DrawerForm fetchCustomers={fetchCustomers} />
          <Text mt={5}>No customers available</Text>
        </SidebarWithHeader>
      )
    }
 
  return (
    <SidebarWithHeader>
      <DrawerForm fetchCustomers={fetchCustomers} />
      <Wrap justify={"center"} spacing={"30px"}>
        {customers.map((c,i)=>(
          <WrapItem key={i}>
          <CardWithImage {...c}
                  fetchCustomers={fetchCustomers}
          ></CardWithImage>
          </WrapItem>
        ))}
        </Wrap>
    </SidebarWithHeader>

        
    
  )
}

export default Customer
