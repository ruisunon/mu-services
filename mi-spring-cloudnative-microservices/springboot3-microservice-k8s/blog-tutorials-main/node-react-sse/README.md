# Server-Sent Events with Node.js and React

This project shows how to implement server sent events using Node.js and React.

## Prerequisites
* Node.js 12+
* React with hooks

## Launch the backend
```shell
cd backend
yarn install
yarn start
```

## Launch the frontend
```shell
cd frontend
yarn install
yarn start
```

## Test
Send a POST request to the route [http://localhost:4650/donate](http://localhost:4650/donate) with the following body:
```json
{
  "amount": 10
}
```
You will see the frontend being update automagically!