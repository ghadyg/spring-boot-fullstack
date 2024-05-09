import React from 'react'

export default function Profile({name,age,...props}) {
  return (
    <div>
    <h1>{name}</h1>
    <h2>{age}</h2>
    {props.children}
    </div>
  )
}
