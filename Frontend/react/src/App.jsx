import Profile from "./Profile.jsx"
import {Spinner,Text,Wrap, WrapItem } from '@chakra-ui/react'
import SidebarWithHeader from "./Components/shared/SideBar.jsx"
import {useEffect,useState} from 'react'
import {getCustomers} from "./services/client.js"
import CardWithImage from "./Components/Card.jsx"



function App() {

  const [customers,setCustomer] = useState([]);
  const [loading,setLoading] = useState(false);

  useEffect(()=>{
    setLoading(true);
    getCustomers().then(res=>{
      console.log(res)
      setCustomer(res.data)
    }).catch(e=>{
      console.log(e)
    })
    .finally(()=>{
      setLoading(false)
    }
  )
    
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
  if(customers.length<=0)
    {
      return(
        <SidebarWithHeader>
                <Text>No customers available</Text>
        </SidebarWithHeader>
      )
    }
 
  return (
    <SidebarWithHeader>
      <Wrap justify={"center"} spacing={"30px"}>
        {customers.map((c,i)=>(
          <WrapItem key={i}>
          <CardWithImage {...c}></CardWithImage>
          </WrapItem>
        ))}
        </Wrap>
    </SidebarWithHeader>

        
    
  )
}

export default App
