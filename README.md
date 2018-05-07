# rapidfs
Very basic key/value database with FST serializing stored in simple `.rapid` files.
It's based on Kryonet for networking stuff.

It has simple command system like
```
set -db database1 -k key1 -v hello world!
```
or
```
drop -db database1
```
or
```
create -db database1
```

It has simple API for sending packets.
Create a client
```kotlin
val client = RapidClient("127.0.0.1", 8190)
    .connect()
```
then create a database or something
```kotlin
val database = Database(client, "test")
    .createOrThrow()
```
Then you can use
```kotlin
database.setOrThrow("key", object)
database.removeOrThrow("key")
database.drop()
```
Remember, standard adding / removing will refresh the database!
You can add/remove without refreshing, and then - update the database.
```kotlin
database.setOrThrow("key", "value", true) // add 'true'
database.setOrThrow("key1", "value", true) // It won't update the file == faster
database.setOrThrow("key2", "value", true) 
database.update() // It will update
```
It looks the same for removal.

Remember, this project won't be supported, it's only a test kotlin project to extend my language knowledge.
I DON'T recommend using this database at all.
