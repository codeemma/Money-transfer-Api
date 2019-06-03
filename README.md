##Rest API for Money Transfer
This is a simple api for money transfer between accounts. The API is built from scratch with pure Java APIs. This app is designed to handle concurrent requests.

###Data Seed
A DataSeed class has been created to populate the data store on start up. Four accounts
are seeded with accountNumbers:`11112222`, `22221111`, `22223333`, `33332222` with account balance of `40000` each.

##EndPoint Created
- Transfer fund endpoint
```
URI - /api/transfer?from={originatingAccountNumber}&to={destinationAccountNumber}&amount={amount}
Method - POST
```
- Get Account endpoint
```
URI - /api/account?accountNumber={accountNumber}
Method - GET
```

server port - `8080` This is set in the mainApp class
###Libraries Used
- Junit4 - for test
- Hamcrest - for test
- Jackson - for json serialization and deserialization