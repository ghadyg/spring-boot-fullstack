import {Drawer, DrawerFooter, Spinner,Text,Wrap, WrapItem } from '@chakra-ui/react'
import SidebarWithHeader from "./Components/shared/SideBar.jsx"
import {useEffect,useState,useCallback,useRef} from 'react'
import {getCustomers, getProfilePicture} from "./services/client.js"
import CardWithImage from "./Components/customer/Card.jsx"
import DrawerForm from "./Components/customer/DrawerForm.jsx"
import { errorNotification } from "./services/notification.js"
import useCustomer from './useCustomer.js'



function Customer() {

  const observer = useRef();

  const pageSize = 10;
  const [offset,setOffset] = useState(0)

  //const [customers,setCustomer] = useState([]);
  const fetchCustomers =()=>{
    setOffset(0);
  }
  // useEffect(()=>{
  //   fetchCustomers();
  // },[])

  const{customers,isLoading,hasMore,err} = useCustomer({pageSize,offset});
  const fetchAnotherPage = useCallback((node)=>{
        if(isLoading) return;
        if(observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver((entries)=>{
          if(entries[0].isIntersecting && hasMore)
            setOffset(offset=>offset+1);          
        }
      )
      if(node) observer.current.observe(node)
    },[isLoading,hasMore])

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
        {customers.map((c,i)=>
       {
        if(i+1 === customers.length)
          {
            return(
              <div ref={fetchAnotherPage} key={i}>
              <WrapItem>
              <CardWithImage {...c}
                      fetchCustomers={fetchCustomers}
              ></CardWithImage>
              </WrapItem>
              </div>
            )
          }
          else
          {
            return(
              <WrapItem key={i}>
              <CardWithImage {...c}
                      fetchCustomers={fetchCustomers}
              ></CardWithImage>
              </WrapItem>
            )
      }
        
        })}
        </Wrap>
        { isLoading && 
      <SidebarWithHeader>
              <Spinner
        thickness='4px'
        speed='0.65s'
        emptyColor='gray.200'
        color='blue.500'
        size='xl'
      />
      </SidebarWithHeader>
      }
    
  
    </SidebarWithHeader>

        
    
  )
}

export default Customer
