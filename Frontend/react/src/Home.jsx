import React from 'react'
import SidebarWithHeader from './Components/shared/SideBar'
import { Text } from '@chakra-ui/react'

export default function Home() {
  return (
    <SidebarWithHeader>
        <Text size={"6xl"}> Dashboard </Text>
    </SidebarWithHeader>
  )
}
