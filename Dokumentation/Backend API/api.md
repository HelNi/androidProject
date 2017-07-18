# Activity #

## /api/activities ##

### `GET` /api/activities.{_format} ###

_Returns all Activities._

Returns all Activities.

#### Requirements ####

**_format**

  - Requirement: json|xml|html



# Authentication #

### `GET` /api/get_token.{_format} ###

_Gets the API token for the current user._

Gets the API token for the current user.

#### Requirements ####

**_format**


#### Filters ####

username:

  * Description: null

password:

  * Description: null



# Entry #

## /api/entries ##

### `GET` /api/entries.{_format} ###

_Gets all Entries for the current user._

Gets all Entries for the current user.

#### Requirements ####

**_format**

  - Requirement: json|xml|html

#### Response ####

activity:

  * type: object (Activity)

activity[id]:

  * type: integer

activity[deprecated]:

  * type: boolean

activity[name]:

  * type: string

activity[category_name]:

  * type: string

start:

  * type: DateTime

end:

  * type: DateTime

description:

  * type: string

user:

  * type: object (User)

user[id]:

  * type: integer

user[username]:

  * type: string

user[username_canonical]:

  * type: string

user[email]:

  * type: string

user[email_canonical]:

  * type: string

user[enabled]:

  * type: boolean

user[salt]:

  * type: string
  * description: The salt to use for hashing.

user[password]:

  * type: string
  * description: Encrypted password. Must be persisted.

user[last_login]:

  * type: DateTime

user[confirmation_token]:

  * type: string
  * description: Random string sent to the user email address in order to verify it.

user[password_requested_at]:

  * type: DateTime

user[roles]:

  * type: array

user[first_name]:

  * type: string

user[last_name]:

  * type: string

user[salutation]:

  * type: string

user[title]:

  * type: string

user[firstName]:

  * type: 

user[lastName]:

  * type: 

user[plainPassword]:

  * type: 

id:

  * type: integer


### `POST` /api/entries.{_format} ###

_Create a new Entry._

Create a new Entry.

#### Requirements ####

**_format**

  - Requirement: json|xml|html

#### Parameters ####

start:

  * type: datetime
  * required: true

end:

  * type: datetime
  * required: true

description:

  * type: string
  * required: true

activity:

  * type: string
  * required: true


## /api/entries/between ##

### `GET` /api/entries/between.{_format} ###


#### Requirements ####

**_format**

  - Requirement: json|xml|html

#### Parameters ####

start:

  * type: datetime
  * required: true

end:

  * type: datetime
  * required: true


## /api/entries/{entry} ##

### `DELETE` /api/entries/{entry}.{_format} ###


#### Requirements ####

**_format**

  - Requirement: json|xml|html
**entry**

  - Type: Entry


### `PUT` /api/entries/{entry}.{_format} ###


#### Requirements ####

**_format**

  - Requirement: json|xml|html
**entry**

  - Type: Entry

#### Parameters ####

start:

  * type: datetime
  * required: true

end:

  * type: datetime
  * required: true

description:

  * type: string
  * required: true

activity:

  * type: string
  * required: true


## /api/entries/{id} ##

### `GET` /api/entries/{id}.{_format} ###

_Gets an Entry by ID._

#### Requirements ####

**_format**

  - Requirement: json|xml|html
**id**

  - Type: int

#### Response ####

activity:

  * type: object (Activity)

activity[id]:

  * type: integer

activity[deprecated]:

  * type: boolean

activity[name]:

  * type: string

activity[category_name]:

  * type: string

start:

  * type: DateTime

end:

  * type: DateTime

description:

  * type: string

user:

  * type: object (User)

user[id]:

  * type: integer

user[username]:

  * type: string

user[username_canonical]:

  * type: string

user[email]:

  * type: string

user[email_canonical]:

  * type: string

user[enabled]:

  * type: boolean

user[salt]:

  * type: string
  * description: The salt to use for hashing.

user[password]:

  * type: string
  * description: Encrypted password. Must be persisted.

user[last_login]:

  * type: DateTime

user[confirmation_token]:

  * type: string
  * description: Random string sent to the user email address in order to verify it.

user[password_requested_at]:

  * type: DateTime

user[roles]:

  * type: array

user[first_name]:

  * type: string

user[last_name]:

  * type: string

user[salutation]:

  * type: string

user[title]:

  * type: string

user[firstName]:

  * type: 

user[lastName]:

  * type: 

user[plainPassword]:

  * type: 

id:

  * type: integer


## /entries/between_for/{userName} ##

### `GET` /entries/between_for/{userName} ###

_Gets all entries for the given user between the two dates_

Gets all entries for the given user between the two dates

#### Requirements ####

**userName**

  - Type: string

#### Parameters ####

start:

  * type: datetime
  * required: true

end:

  * type: datetime
  * required: true



# User #

## /api/user/self ##

### `GET` /api/user/self.{_format} ###


#### Requirements ####

**_format**

  - Requirement: json|xml|html


## /api/users/{user} ##

### `GET` /api/users/{user}.{_format} ###

_Gets a user by ID_

Gets a user by ID

#### Requirements ####

**_format**

  - Requirement: json|xml|html
**user**

  - Type: User

#### Response ####

firstName:

  * type: 

lastName:

  * type: 

salutation:

  * type: string

title:

  * type: string

username:

  * type: string

email:

  * type: string

plainPassword:

  * type: 

id:

  * type: integer

username_canonical:

  * type: string

email_canonical:

  * type: string

enabled:

  * type: boolean

salt:

  * type: string
  * description: The salt to use for hashing.

password:

  * type: string
  * description: Encrypted password. Must be persisted.

last_login:

  * type: DateTime

confirmation_token:

  * type: string
  * description: Random string sent to the user email address in order to verify it.

password_requested_at:

  * type: DateTime

roles:

  * type: array

first_name:

  * type: string

last_name:

  * type: string
