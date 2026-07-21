# BookMyShow Backend Documentation
# Part 1 - Foundation

Files Covered

1. App.jsx
2. apis/index.js
3. AuthContext.jsx
4. LocationContext.jsx
5. SeatContext.jsx

---

# 1. App.jsx

## Purpose

This is the entry point of the React application.

Its responsibilities are:

- Load current logged in user
- Configure routing
- Protect authenticated pages
- Display Header/Footer
- Display Loading Screen until user information is fetched

Think of it as Spring Boot's DispatcherServlet + Security configuration.

---

## Route Structure

Public Routes

GET /

Purpose

Home page.

---

GET /movies

Purpose

Display all available movies.

---

GET /movies/:state/:movieName/:id/ticket

Example

/movies/Madhya Pradesh/f1-the-movie/8/ticket

Parameters

state

Current user location.

movieName

Only used for SEO.

Backend never needs this.

id

Movie Primary Key.

Backend Requirement

MovieController

GET /movies/{id}

---

Protected Routes

User must be authenticated.

Otherwise frontend redirects to "/"

### Seat Layout

Route

/movies/:movieId/:movieName/:state/theater/:theaterId/show/:showId/seat-layout

Parameters

movieId

Movie ID

movieName

SEO only

state

Current location

theaterId

Theater ID

showId

Show ID

Backend APIs required

GET /shows/{showId}

Socket Connection

Seat Lock APIs

Booking APIs

---

### Profile

Route

/profile/:id/:tab

Parameters

id

User ID

tab

profile

booking

etc.

Backend

GET /users/me

GET /book

---

Checkout

Route

/shows/:showId/:state/checkout

Parameters

showId

Current Show

state

Current Location

Backend

POST /payment/create-order

POST /payment/verify-payment

POST /book

---

Authentication Guard

PrivateRoute

Logic

IF authenticated

Allow page

ELSE

Redirect Home

Equivalent Spring Boot

Spring Security

JWT Authentication Filter

Session Validation

---

Application Startup Flow

Application Starts

↓

useLoadUser()

↓

GET /users/me

↓

Valid Session ?

YES

↓

Store User

↓

Render App

NO

↓

Continue as Guest

---

Header/Footer

Displayed on

Home

Movies

Movie Details

Profile

Hidden on

Seat Layout

Checkout

Reason

Those pages use a completely different layout.

---

# 2. apis/index.js

Purpose

Contains every backend endpoint used by the frontend.

This file tells us the entire backend contract.

---

Movie APIs

GET /movies/recommended

Purpose

Return top recommended movies.

Expected Response

Movie[]

---

GET /movies

Purpose

Return all movies.

Expected Response

Movie[]

---

GET /movies/{id}

Purpose

Return complete movie details.

Expected Response

Movie

---

Show APIs

GET /shows

Query Parameters

movieId

state

date

Example

GET /shows?movieId=4&state=Madhya Pradesh&date=21-07-2026

Purpose

Return all theaters and timings for that movie.

---

GET /shows/{id}

Purpose

Return one show.

Includes

Movie

Theater

Seat Layout

Pricing

---

Authentication APIs

POST /auth/send-otp

Request

{
email
}

Response

{
hash,
email
}

---

POST /auth/verify-otp

Request

{
email,
hash,
otp
}

Response

{
user
}

---

PUT /users/activate/{id}

Request

{
name,
phone
}

Purpose

Complete profile.

---

POST /auth/logout

Purpose

Destroy session.

---

GET /users/me

Purpose

Return current logged in user.

Used every time application starts.

---

Payment APIs

POST /payment/create-order

Purpose

Create Razorpay Order.

---

POST /payment/verify-payment

Purpose

Verify payment signature.

---

Booking APIs

POST /book

Purpose

Create booking.

---

GET /book

Purpose

Return booking history.

---

Refresh Token Logic

Whenever frontend receives

401 Unauthorized

It automatically calls

GET /auth/refresh-token

If successful

Retry original request.

Backend Requirement

JWT Refresh Token

OR

Session Refresh Endpoint

---

# 3. AuthContext.jsx

Purpose

Complete authentication logic.

Instead of every component handling login,

everything goes through AuthContext.

---

Authentication Flow

User enters Email

↓

POST /auth/send-otp

↓

Receive hash

↓

Enter OTP

↓

POST /auth/verify-otp

↓

Receive User

↓

activateUser ?

YES

↓

Login Complete

NO

↓

Collect Name + Phone

↓

PUT /users/activate/{id}

↓

Login Complete

---

State Stored

step

Current authentication screen.

1

Email

2

OTP

3

Profile Completion

---

showModal

Controls login popup.

---

user

Current logged in user.

---

auth

true

User authenticated.

false

Guest.

---

Backend APIs Used

POST /auth/send-otp

POST /auth/verify-otp

PUT /users/activate/{id}

POST /auth/logout

---

User Object Required

id

email

name

phone

activateUser

---

Logout Flow

POST /auth/logout

↓

Remove Session

↓

Frontend clears User

↓

Redirect "/"

---

# 4. LocationContext.jsx

Purpose

Automatically detect user's State.

Backend never calculates this.

Browser provides

Latitude

Longitude

↓

OpenStreetMap Reverse Geocoding

↓

State Name

↓

Stored inside Context

Example

"Madhya Pradesh"

---

Later Used In

Movie Search

Show Search

Booking

Checkout

---

Backend Receives

state

inside

GET /shows

Example

GET /shows?state=Madhya Pradesh

---

Possible Backend Improvement

Instead of sending state

Use

cityId

or

locationId

More scalable.

---

# 5. SeatContext.jsx

Purpose

Temporary storage while booking.

No backend calls happen here.

Stores

Current Show

Selected Seats

---

shows

Contains

Current Show Object

Movie

Theater

Seat Layout

Time

Date

---

selectedSeats

Example

[
"A1",
"A2",
"B6"
]

Later sent to

POST /book

---

Booking Flow

User opens Seat Layout

↓

Choose Seats

↓

selectedSeats updated

↓

Checkout

↓

Payment

↓

POST /book

↓

Booking Created

---

Backend Notes

Frontend trusts

selectedSeats

Backend should NEVER trust it.

Always verify

Seat exists

Seat available

Seat not locked

Seat not booked

Price correct

before booking.

---

Summary

Files Completed

✔ App.jsx

✔ apis/index.js

✔ AuthContext.jsx

✔ LocationContext.jsx

✔ SeatContext.jsx

Backend Modules Identified

Authentication

Movie

Show

Booking

Payment

Location

Seat Selection

Session Management

Refresh Token




# BookMyShow Backend Documentation
# Part 2 - Hooks, Utils, Constants & Socket

Files Covered

1. useCountdown.jsx
2. useCurrentStateLocation.js
3. useLoadUser.js
4. constants.js
5. utils/index.js
6. socket.js

---

# 1. useCountdown.jsx

## Purpose

A reusable countdown timer.

Currently used during

- OTP Verification
- Checkout Seat Hold Timer

The hook only controls UI.

It DOES NOT communicate with backend.

---

## Working

Input

```java
initialTimeInSeconds
```

Example

```java
120
```

Means

2 Minutes

---

Every Second

```text
timeInSeconds--

↓

Calculate Minutes

↓

Calculate Seconds

↓

Return Formatted Time
```

Example

```
02:00

↓

01:59

↓

01:58

↓

...

↓

00:00
```

---

## Returned Values

displayTime

Example

```
01:37
```

---

isExpired

Boolean

true

Countdown finished.

false

Still running.

---

## Current Usage

OTP Expiry

```
2 Minutes
```

Checkout

```
5 Minutes
```

---

## Backend Recommendation

Frontend timer should NEVER be trusted.

Instead

Store seat lock time inside database or Redis.

Example

```
Seat Locked

↓

Current Timestamp

↓

+5 Minutes

↓

Auto Release
```

Even if user closes browser

Server should unlock seats automatically.

---

Spring Boot Recommendation

Use

```
Scheduled Task

OR

Redis Expiry

OR

Quartz Scheduler
```

Never depend on frontend timer.

---

# 2. useCurrentStateLocation.js

## Purpose

Experimental hook.

Currently unfinished.

Not used anywhere.

---

Current Flow

Browser

↓

Latitude

↓

Longitude

↓

Console Log

Nothing else happens.

---

Backend Impact

None.

Can safely ignore.

---

Possible Future Improvement

Could directly call

Google Maps API

or

OpenStreetMap

Then return

```
City

State

Country
```

Instead of only logging coordinates.

---

# 3. useLoadUser.js

## Purpose

Automatically authenticate user whenever application starts.

This is one of the most important hooks.

Equivalent in backend

```
Check Session

OR

Validate JWT
```

---

Execution Flow

Application Starts

↓

useLoadUser()

↓

GET /users/me

↓

Success ?

YES

↓

Store User

↓

auth=true

↓

Application Loads

NO

↓

Continue as Guest

---

API Used

GET

/users/me

---

Expected Success Response

```
{
    id,
    name,
    email,
    phone,
    activateUser
}
```

---

Failure Response

401 Unauthorized

Frontend simply logs error.

User continues as Guest.

---

Backend Requirement

Whenever

GET /users/me

is called

Backend must

Validate

JWT

or

Session Cookie

Return

Current User

Otherwise

401

---

Why this hook exists

Without it

Refreshing browser would log user out.

This hook restores authentication.

---

Spring Boot Recommendation

Controller

UserController

Method

GET /users/me

Service

Extract User

↓

Validate Token

↓

Return DTO

---

# 4. constants.js

## Purpose

Contains application constants.

Most are frontend only.

Some reveal backend requirements.

---

Languages

```
Hindi

English

Tamil

Telugu

Japanese

Punjabi

Bengali
```

Backend Recommendation

Movie Entity

```
languages

List<String>
```

or

Many-to-Many

Language Entity

---

Movie Filters

Current filters

```
2D

3D

Wheelchair Friendly

Premium Seats

IMAX

Laser

4DX

Dolby Atmos
```

Future Backend API

GET

```
/movies

?language=Hindi

&format=IMAX

&genre=Action
```

---

Profile Tabs

```
profile

booking
```

Backend

No impact.

Frontend navigation only.

---

Country Codes

Frontend currently stores all.

Backend

No requirement.

---

Razorpay Script

```
https://checkout.razorpay.com/v1/checkout.js
```

Frontend loads SDK.

Backend responsibility

Only

Create Order

Verify Payment

Never loads SDK.

---

Dummy Data

Movies

Events

Theatres

Orders

Banners

These are placeholder data.

Ignore while building backend.

Real data comes from APIs.

---

Backend Important Fields Discovered

Movie

```
title

rating

votes

genre

languages

age

format

duration

posterUrl
```

Show

```
audioType
```

Booking

```
paymentMethod

bookingFee
```

---

# 5. utils/index.js

Purpose

Contains shared business logic.

Some logic SHOULD move to backend.

---

formatReleaseDate()

Purpose

Convert

```
2026-07-21

↓

21 Jul, 2026
```

Backend

Not required.

Frontend formatting only.

---

formatedTodayDate()

Returns

```
DD-MM-YYYY
```

Example

```
21-07-2026
```

Used while requesting shows.

Backend expects

```
date

21-07-2026
```

---

Seat Type Mapping

Current Logic

```
Row A

↓

NORMAL

Row B

↓

EXECUTIVE

Row C

↓

EXECUTIVE

Row D

↓

EXECUTIVE

Row E

↓

PREMIUM
```

Example

```
A1

↓

NORMAL

E7

↓

PREMIUM
```

---

Backend Recommendation

Never calculate on frontend.

Seat Entity should already know

```
type

price
```

---

Seat Prices

Current

```
PREMIUM

510

EXECUTIVE

290

NORMAL

180
```

Problem

Prices hardcoded.

Backend Recommendation

Store

ShowPrice

or

SeatPrice

inside database.

---

groupSeatsByType()

Input

```
A1

A2

B5

E2
```

Output

```
NORMAL

A1

A2

EXECUTIVE

B5

PREMIUM

E2
```

Used during checkout.

Backend could return grouped seats directly.

---

calculateTotalPrice()

Current Flow

```
Selected Seats

↓

Calculate Base

↓

Tax

5%

↓

Total
```

Returns

```
base

tax

total
```

Problem

User can manipulate frontend.

Backend MUST recalculate.

Never trust frontend totals.

---

Spring Boot Recommendation

BookingService

```
Fetch Seat Prices

↓

Calculate Base

↓

Calculate Tax

↓

Calculate Total

↓

Save Booking
```

Ignore any total sent by frontend.

---

# 6. socket.js

Purpose

Creates Socket.IO connection.

```
socket = io("http://localhost:9000")
```

Everything related to realtime seats uses this object.

---

Socket Events

Client Emits

```
join-show

lock-seats

unlock-seats
```

Server Emits

```
locked-seats-initials

seat-locked

seat-unlocked

seat-locked-failed
```

---

Backend Recommendation

Spring Boot itself doesn't provide Socket.IO.

Possible options

Option 1

Socket.IO Server

(Node)

Option 2

Spring WebSocket

(STOMP)

Option 3

Redis Pub/Sub

+ WebSocket

If converting everything to Spring

I'd recommend

Spring WebSocket

instead of Socket.IO.

---

Realtime Seat Flow

User Opens Seat Layout

↓

join-show

↓

Server joins room

↓

User selects seats

↓

lock-seats

↓

Server validates

↓

Broadcast seat-locked

↓

Other users immediately see seat unavailable

↓

Booking Complete

↓

unlock-seats

↓

Broadcast updated seat status

---

Important Backend Rule

Never lock seats only in memory.

Store lock state in

Redis

or

Database

Otherwise server restart

↓

All locks disappear.

---

# Summary

Files Completed

✔ useCountdown.jsx

✔ useCurrentStateLocation.js

✔ useLoadUser.js

✔ constants.js

✔ utils/index.js

✔ socket.js

---

## New Backend Requirements Identified

Authentication

- Auto session restore
- JWT validation
- GET /users/me

Seat Module

- Seat type
- Seat pricing
- Seat grouping
- Seat lock expiry

Movie Module

- Languages
- Formats
- Genres
- Age Rating
- Audio Type

Booking Module

- Backend price calculation
- Tax calculation
- Booking fee

Realtime Module

- WebSocket / Socket.IO
- Join room
- Lock seat
- Unlock seat
- Broadcast changes

---

## Spring Boot Improvements over Frontend

✔ Don't trust frontend price calculation.

✔ Don't trust frontend countdown timer.

✔ Store seat prices in database.

✔ Store seat locks in Redis or Database.

✔ Validate every booking request on server.

✔ Recalculate total before payment verification.

✔ Implement proper JWT/session validation for GET /users/me.



# BookMyShow Backend Documentation
# Part 3 - Main Pages

Files Covered

1. Home.jsx
2. Movies.jsx
3. MovieDetails.jsx
4. SeatLayout.jsx
5. Checkout.jsx
6. Profile.jsx

---

# 1. Home.jsx

## Purpose

Acts as the landing page of the application.

No backend logic exists directly inside this file.

It only loads three components.

```
BannerSlider

↓

Recommended Movies

↓

Live Events
```

---

## Components Loaded

### BannerSlider

Displays advertisements.

Backend Requirement

None.

Future

```
GET /banners
```

---

### Recommended

Displays recommended movies.

Backend API

```
GET /movies/recommended
```

---

### LiveEvents

Displays events.

Currently static.

Future API

```
GET /events
```

---

## Backend Responsibility

Only Recommended Movies require backend.

Banner

Live Events

are currently static.

---

# 2. Movies.jsx

## Purpose

Displays all available movies.

This is the Movie Listing page.

---

## Application Flow

User opens

```
/movies
```

↓

GET /movies

↓

Receive Movie[]

↓

Display Movie Cards

---

## Backend API

```
GET /movies
```

---

## Expected Response

```json
[
    {
        "id":1,
        "title":"F1",
        "posterUrl":"...",
        "rating":9.3,
        "votes":"12K",
        "languages":[...],
        "age":"UA16+"
    }
]
```

---

## Movie Object Fields Used

id

title

posterUrl

rating

votes

languages

age

---

## Future Backend

Movie Filters currently don't work.

Recommended API

```
GET /movies

?language=Hindi

&genre=Action

&format=IMAX

&city=Indore
```

---

## Spring Boot Recommendation

MovieController

```
GET /movies
```

MovieService

```
findAllMovies()

↓

Map to DTO

↓

Return
```

---

# 3. MovieDetails.jsx

## Purpose

Displays complete details of one movie.

Also starts the ticket booking process.

---

## Route

```
/movies/:state/:movieName/:id/ticket
```

Parameters

state

Current Location

movieName

SEO only

id

Movie Primary Key

---

## Data Flow

User clicks Movie

↓

Movie ID

↓

GET /movies/{id}

↓

Movie Object

↓

Render Details

↓

Load TheaterTimings Component

↓

GET /shows

---

## Backend API

```
GET /movies/{id}
```

---

## Expected Movie Response

```json
{
    "id":1,
    "title":"F1",

    "description":"...",

    "posterUrl":"...",

    "rating":9.3,

    "votes":"10K",

    "languages":[
        "English",
        "Hindi"
    ],

    "format":[
        "2D",
        "IMAX"
    ],

    "genre":[
        "Action",
        "Sports"
    ],

    "duration":"2h 36m",

    "releaseDate":"2026-07-21",

    "certification":"UA16+"
}
```

---

## Fields Used

Poster

Title

Rating

Votes

Languages

Formats

Genre

Duration

Certification

Release Date

Description

---

## After Movie Loads

TheaterTimings component receives

```
movieId
```

Then calls

```
GET /shows
```

---

## Future Improvement

Instead of

```
GET /movies/{id}

↓

GET /shows
```

Backend could return

Movie

+

Available Shows

in one request.

Less network traffic.

---

# 4. SeatLayout.jsx

## Purpose

Most important page in entire application.

Handles

Seat Rendering

Realtime Locking

Realtime Unlocking

Seat Selection

Navigation to Checkout

---

## Route

```
/movies/{movieId}/theater/{theaterId}/show/{showId}/seat-layout
```

---

## Backend API

```
GET /shows/{showId}
```

---

## Expected Response

```text
Show

↓

Movie

↓

Theater

↓

Seat Layout
```

---

## Seat Layout Structure

```
Show

↓

seatLayout[]

↓

Seat Type

↓

Rows

↓

Seats
```

Example

```
PREMIUM

↓

Row E

↓

Seat 1

Seat 2

Seat 3
```

---

## Seat Object

```
number

status
```

Possible Status

```
AVAILABLE

BOOKED
```

Frontend additionally treats

```
LOCKED
```

through socket events.

---

## Seat Selection

Whenever user clicks

```
A5
```

Frontend

Adds

```
selectedSeats[]
```

or

Removes it.

Nothing sent to backend yet.

---

## Socket Connection

Immediately after page opens

Client Emits

```
join-show
```

Payload

```
showId
```

---

Server Responds

```
locked-seats-initials
```

Payload

```
seatIds[]
```

These seats become disabled.

---

Realtime Lock

Another user selects seats

↓

Server Emits

```
seat-locked
```

↓

Frontend disables seats instantly.

---

Realtime Unlock

Booking completed

OR

Timer expired

↓

Server Emits

```
seat-unlocked
```

↓

Frontend enables seats.

---

Lock Failure

Server emits

```
seat-locked-failed
```

Payload

```
requestedSeats

alreadyLocked
```

Frontend

Displays Toast.

---

## Backend Responsibilities

Validate

Seat Exists

↓

Seat Available

↓

Seat Not Locked

↓

Lock Seats

↓

Broadcast Changes

---

## Important Recommendation

Never store locks in RAM.

Use

Redis

or

Database

Otherwise

Server Restart

↓

All seats unlocked.

---

# 5. Checkout.jsx

## Purpose

Handles

Payment

Booking

Seat Unlock

This page should NEVER trust frontend calculations.

---

## Preconditions

Must have

User

Show

Selected Seats

Otherwise

Redirect Home.

---

## Checkout Flow

Seat Layout

↓

Selected Seats

↓

Calculate Total

↓

Load Razorpay SDK

↓

POST /payment/create-order

↓

Receive Order

↓

Open Razorpay

↓

Payment Success

↓

POST /payment/verify-payment

↓

POST /book

↓

unlock-seats

↓

Profile

---

## Payment API

```
POST /payment/create-order
```

Request

```json
{
    "amount":913.50
}
```

---

Response

```json
{
    "id":"order_xxx",

    "amount":91350,

    "currency":"INR"
}
```

---

## Verify Payment

```
POST /payment/verify-payment
```

Receives

Razorpay Response

Backend verifies

Signature

Order

Payment

---

## Booking API

```
POST /book
```

Request

```json
{
    "showId":4,

    "seats":[
        "A1",
        "A2"
    ],

    "paymentId":"pay_xxx",

    "bookingFee":{

        "ticketPrice":870,

        "convenience":43.5,

        "total":913.5
    }
}
```

---

## Backend Should Ignore

ticketPrice

convenience

total

These should ALWAYS be recalculated.

Never trust frontend.

---

## Booking Process

Validate Payment

↓

Validate Seats

↓

Calculate Price

↓

Mark Seats BOOKED

↓

Save Booking

↓

Unlock Seats

↓

Return Success

---

## Timeout

Frontend

Starts

5 Minute Timer

If expired

```
unlock-seats
```

Backend should NOT wait for frontend.

Implement server-side timeout.

---

## Razorpay

Frontend loads SDK.

Backend responsibilities

Create Order

Verify Signature

Persist Payment

Nothing else.

---

# 6. Profile.jsx

## Purpose

Displays

User Information

Booking History

Logout

---

## Route

```
/profile/{id}/{tab}
```

Possible Tabs

```
profile

booking
```

---

## User Information

Displays

Name

Email

Phone

---

## Backend API

```
GET /users/me
```

Already loaded earlier.

No additional request.

---

## Logout

```
POST /auth/logout
```

↓

Destroy Session

↓

Redirect Home

---

## Booking History

Loads

BookingHistory Component

↓

GET /book

---

## Future Backend APIs

Update Profile

```
PUT /users/profile
```

---

Update Phone

```
PUT /users/phone
```

---

Update Email

```
PUT /users/email
```

---

# Complete User Journey

```
Home

↓

Recommended Movies

↓

Movies

↓

Movie Details

↓

GET /movies/{id}

↓

GET /shows

↓

Seat Layout

↓

Socket Join

↓

Seat Lock

↓

Checkout

↓

Create Razorpay Order

↓

Verify Payment

↓

Create Booking

↓

Unlock Seats

↓

Booking History

↓

Logout
```

---

# Backend Modules Identified

Movie Module

```
GET /movies

GET /movies/{id}

GET /movies/recommended
```

---

Show Module

```
GET /shows

GET /shows/{id}
```

---

Booking Module

```
POST /book

GET /book
```

---

Payment Module

```
POST /payment/create-order

POST /payment/verify-payment
```

---

Authentication Module

```
POST /auth/send-otp

POST /auth/verify-otp

GET /users/me

POST /auth/logout
```

---

Realtime Module

```
join-show

lock-seats

unlock-seats

seat-locked

seat-unlocked
```

---

## Spring Boot TODO

### Movie

☐ MovieController

☐ MovieService

☐ MovieRepository

☐ MovieDTO

---

### Show

☐ ShowController

☐ ShowService

☐ SeatLayout Builder

---

### Seat

☐ Seat Lock Logic

☐ Redis Lock

☐ Lock Expiry

---

### Booking

☐ BookingController

☐ BookingService

☐ Payment Verification

☐ Price Calculation

☐ BookingRepository

---

### Payment

☐ Razorpay Integration

☐ Signature Verification

☐ Order Creation

---

### User

☐ Current User API

☐ Logout API

☐ Booking History API




# BookMyShow Backend Documentation
# Part 4 - Authentication & Movie Components

Files Covered

1. StepEmail.jsx
2. StepOTP.jsx
3. StepAccountCreation.jsx
4. MovieCard.jsx
5. MovieFilters.jsx
6. MovieList.jsx
7. TheaterTimings.jsx

-------------------------------------------------------------------------------

# 1. StepEmail.jsx

## Purpose

This is the first screen shown when a user clicks **Sign In**.

The application uses **passwordless authentication**.

Instead of

```
Email
Password
```

it only asks for

```
Email

↓

OTP
```

-------------------------------------------------------------------------------

## Authentication Flow

```
User enters email

↓

Frontend validates email exists

↓

POST /auth/send-otp

↓

Backend sends OTP to email

↓

Backend returns hash

↓

Frontend stores hash

↓

Move to OTP Screen
```

-------------------------------------------------------------------------------

## Backend API

```
POST /auth/send-otp
```

### Request

```json
{
    "email":"aman@gmail.com"
}
```

-------------------------------------------------------------------------------

### Expected Response

```json
{
    "hash":"8bd71c89....",
    "email":"aman@gmail.com"
}
```

Hash is **NOT** shown to user.

Frontend stores it temporarily.

-------------------------------------------------------------------------------

## Why Hash?

Instead of storing OTP on frontend

Backend generates

```
OTP

↓

Hash(OTP + Email + Expiry)

↓

Return Hash

↓

Frontend sends hash back while verifying
```

Backend never trusts frontend.

-------------------------------------------------------------------------------

## Backend Responsibilities

Validate

Email format

↓

Generate OTP

↓

Store OTP

↓

Send Email

↓

Return Hash

-------------------------------------------------------------------------------

## Suggested Spring Boot Design

Controller

```
AuthController

sendOtp()
```

Service

```
OtpService

↓

Generate OTP

↓

Save OTP

↓

Send Email
```

Repository

```
OtpRepository

or

Redis
```

-------------------------------------------------------------------------------

# 2. StepOTP.jsx

## Purpose

Verifies OTP received on email.

-------------------------------------------------------------------------------

## Application Flow

```
User enters

4 digits

↓

POST /auth/verify-otp

↓

OTP Correct ?

YES

↓

Receive User

↓

activateUser ?

YES

↓

Login Complete

NO

↓

Account Creation Screen
```

-------------------------------------------------------------------------------

## Backend API

```
POST /auth/verify-otp
```

-------------------------------------------------------------------------------

### Request

```json
{
    "email":"aman@gmail.com",
    "hash":"abc123",
    "otp":1234
}
```

-------------------------------------------------------------------------------

### Success Response

```json
{
    "user":{

        "id":4,

        "name":"Aman",

        "email":"aman@gmail.com",

        "phone":"9876543210",

        "activateUser":true
    }
}
```

-------------------------------------------------------------------------------

## OTP Rules

Length

```
4 Digits
```

Expiry

```
2 Minutes
```

-------------------------------------------------------------------------------

## Resend OTP

Current frontend

Placeholder only.

Future API

```
POST /auth/resend-otp
```

-------------------------------------------------------------------------------

## Backend Responsibilities

Verify

Hash

↓

OTP

↓

Expiry

↓

Return User

-------------------------------------------------------------------------------

## Security Recommendation

Don't store OTP

Store

```
Hashed OTP

+

Expiry Time
```

inside Redis.

-------------------------------------------------------------------------------

# 3. StepAccountCreation.jsx

## Purpose

Executed only once.

When user signs in for first time.

-------------------------------------------------------------------------------

## Why?

OTP verifies

```
Email
```

Only.

Application still needs

```
Name

Phone
```

-------------------------------------------------------------------------------

## Backend API

```
PUT /users/activate/{id}
```

-------------------------------------------------------------------------------

### Request

```json
{
    "name":"Aman",

    "phone":"9876543210"
}
```

-------------------------------------------------------------------------------

### Backend Responsibilities

Update User

```
name

phone

activateUser=true
```

-------------------------------------------------------------------------------

### Suggested Validation

Name

```
Not Empty
```

Phone

```
10 digits

Unique
```

-------------------------------------------------------------------------------

## Database Changes

Before

```
User

id

email

activateUser=false
```

After

```
User

id

email

name

phone

activateUser=true
```

-------------------------------------------------------------------------------

# 4. MovieCard.jsx

## Purpose

Displays one movie inside Movie List.

-------------------------------------------------------------------------------

## Fields Required

Movie

```
id

title

posterUrl

rating

votes

age

languages
```

-------------------------------------------------------------------------------

## Navigation

Click Movie

↓

```
/movies/{state}/{movie-name}/{movieId}/ticket
```

-------------------------------------------------------------------------------

Example

```
/movies/Madhya Pradesh/f1-the-movie/8/ticket
```

-------------------------------------------------------------------------------

## Interesting Observation

Frontend converts

```
F1: The Movie
```

into

```
f1-the-movie
```

Only for SEO.

Backend only requires

```
movieId
```

-------------------------------------------------------------------------------

# 5. MovieFilters.jsx

## Purpose

Displays movie filters.

Currently

UI Only.

-------------------------------------------------------------------------------

## Available Filters

Language

Genre

Format

Browse by Cinema

-------------------------------------------------------------------------------

## Backend Recommendation

Instead of frontend filtering

Support

```
GET /movies
```

Query Parameters

```
language

genre

format

cinema
```

-------------------------------------------------------------------------------

Example

```
GET /movies

?language=Hindi

&genre=Action

&format=IMAX

&cinema=PVR
```

-------------------------------------------------------------------------------

## SQL Example

```
SELECT *

FROM movie

WHERE

language='Hindi'

AND genre='Action';
```

-------------------------------------------------------------------------------

# 6. MovieList.jsx

## Purpose

Displays list of movies.

-------------------------------------------------------------------------------

## Data Flow

```
GET /movies

↓

Movie[]

↓

MovieCard

↓

Click

↓

MovieDetails
```

-------------------------------------------------------------------------------

## Additional Feature

Displays

```
Coming Soon
```

Currently static.

Future API

```
GET /movies/upcoming
```

-------------------------------------------------------------------------------

## Movie DTO Required

```
id

title

posterUrl

rating

votes

languages

age
```

-------------------------------------------------------------------------------

# 7. TheaterTimings.jsx

## Purpose

One of the most important components.

Displays

Available Theatres

+

Available Show Timings

-------------------------------------------------------------------------------

## User Flow

Movie Details

↓

Choose Date

↓

GET /shows

↓

Receive Shows

↓

Display Theatre

↓

Display Timings

↓

Choose Timing

↓

Seat Layout

-------------------------------------------------------------------------------

## Backend API

```
GET /shows
```

-------------------------------------------------------------------------------

### Query Parameters

```
movieId

state

date
```

-------------------------------------------------------------------------------

Example

```
GET /shows

?movieId=8

&state=Madhya Pradesh

&date=22-07-2026
```

-------------------------------------------------------------------------------

## Expected Response

```
Movie

↓

Theaters[]

↓

Shows[]
```

-------------------------------------------------------------------------------

Example

```json
[
    {

        "movie":{

            "id":8,

            "title":"F1"
        },

        "theater":{

            "theaterDetails":{

                "id":3,

                "name":"PVR Treasure Island",

                "logo":"..."
            },

            "shows":[

                {

                    "id":12,

                    "startTime":"10:30 AM",

                    "audioType":"DOLBY"
                },

                {

                    "id":13,

                    "startTime":"02:00 PM",

                    "audioType":"IMAX"
                }

            ]
        }
    }
]
```

-------------------------------------------------------------------------------

## Date Selection

Frontend generates

```
Today

+

Next 6 Days
```

Whenever user changes date

Another

```
GET /shows
```

request is sent.

-------------------------------------------------------------------------------

## Authentication Check

Before opening Seat Layout

Frontend checks

```
auth==true
```

If

false

↓

Open Login Modal

Otherwise

↓

Navigate

```
Seat Layout
```

-------------------------------------------------------------------------------

## Navigation

```
/movies/{movieId}/{movieName}/{state}/theater/{theaterId}/show/{showId}/seat-layout
```

Backend only requires

```
movieId

theaterId

showId
```

Movie Name

State

exist mainly for routing and readability.

-------------------------------------------------------------------------------

## Backend Responsibilities

Return

```
Movie

↓

Theater

↓

Shows
```

Filter By

```
Movie

State

Date
```

Sort Shows

```
Morning

↓

Afternoon

↓

Evening

↓

Night
```

Hide

Expired Shows.

-------------------------------------------------------------------------------

# Backend Modules Discovered

Authentication

```
POST /auth/send-otp

POST /auth/verify-otp

POST /auth/resend-otp
```

-------------------------------------------------------------------------------

Users

```
PUT /users/activate/{id}
```

-------------------------------------------------------------------------------

Movies

```
GET /movies

GET /movies/upcoming

Future Filters
```

-------------------------------------------------------------------------------

Shows

```
GET /shows

movieId

state

date
```

-------------------------------------------------------------------------------

## Spring Boot TODO

Authentication

☐ OTP Generation

☐ OTP Verification

☐ Email Service

☐ Redis OTP Storage

☐ Resend OTP

-------------------------------------------------------------------------------

User

☐ Activate User

☐ Validate Phone

☐ Validate Name

-------------------------------------------------------------------------------

Movie

☐ Movie Listing

☐ Movie Filters

☐ Upcoming Movies

-------------------------------------------------------------------------------

Show

☐ Show Search

☐ Date Filter

☐ State Filter

☐ Hide Expired Shows



# BookMyShow Backend Documentation
# Part 5 - Shared Components & Remaining Components

Files Covered

1. Header.jsx (Main Header)
2. SignInModal.jsx
3. Header.jsx (Seat Layout Header)
4. Footer.jsx (Seat Layout Footer)
5. BookingHistory.jsx
6. Recommended.jsx
7. BannerSlider.jsx
8. LiveEvents.jsx
9. Footer.jsx (Main Footer)
10. FullScreenLoader.jsx
11. Seat.jsx

-------------------------------------------------------------------------------

# 1. Header.jsx (Main Header)

## Purpose

This is the global navigation bar of the application.

Appears on

```
Home

Movies

Movie Details

Profile
```

Does NOT appear on

```
Seat Layout

Checkout
```

because those pages use a custom header.

-------------------------------------------------------------------------------

## Responsibilities

Displays

```
Logo

↓

Search Bar

↓

Current Location

↓

Authentication Status

↓

Navigation Links
```

-------------------------------------------------------------------------------

## User Authentication

If

```
auth == true
```

Display

```
Hi, Aman

↓

Profile
```

Otherwise

```
Sign In
```

-------------------------------------------------------------------------------

## Backend Dependency

Current User

```
GET /users/me
```

Required Fields

```
id

name
```

-------------------------------------------------------------------------------

## Location Dependency

Uses

```
LocationContext
```

Displays

```
Current State
```

Example

```
Madhya Pradesh
```

Backend doesn't provide it.

-------------------------------------------------------------------------------

## Search Bar

Current Status

```
UI Only
```

Recommended Backend API

```
GET /movies/search
```

Query

```
?q=f1
```

Expected Response

```json
[
    {
        "id":8,
        "title":"F1"
    }
]
```

-------------------------------------------------------------------------------

## Navigation

Routes

```
Movies

Events

Sports

Activities

Offers
```

Currently only

Movies

is functional.

-------------------------------------------------------------------------------

# 2. SignInModal.jsx

## Purpose

Acts as the Authentication Container.

It doesn't authenticate users.

It simply controls which authentication step is currently visible.

-------------------------------------------------------------------------------

## Authentication Steps

```
Step 1

Email

↓

Step 2

OTP

↓

Step 3

Account Creation
```

-------------------------------------------------------------------------------

## Flow

```
Header

↓

Sign In

↓

Open Modal

↓

StepEmail

↓

StepOTP

↓

StepAccountCreation

↓

Close Modal
```

-------------------------------------------------------------------------------

## Backend Impact

None directly.

All backend communication happens inside

```
StepEmail

StepOTP

StepAccountCreation
```

-------------------------------------------------------------------------------

# 3. Header.jsx (Seat Layout)

## Purpose

Displays selected movie information while booking.

-------------------------------------------------------------------------------

## Required Show Object

Movie

```
title
```

Theater

```
name

city

state
```

Show

```
date

startTime

audioType
```

-------------------------------------------------------------------------------

## Backend API

```
GET /shows/{showId}
```

Must return

```json
{
    "movie":{

        "title":"F1"
    },

    "theater":{

        "name":"PVR",

        "city":"Indore",

        "state":"MP"
    },

    "date":"22-07-2026",

    "startTime":"10:30 AM",

    "audioType":"DOLBY"
}
```

-------------------------------------------------------------------------------

## Authentication

Header again checks

```
auth
```

If logged in

```
Hi User
```

Else

```
Sign In
```

-------------------------------------------------------------------------------

# 4. Footer.jsx (Seat Layout)

## Purpose

Controls transition

```
Seat Layout

↓

Checkout
```

-------------------------------------------------------------------------------

## Flow

User selects seats

↓

Click Proceed

↓

Emit

```
lock-seats
```

↓

Navigate

```
Checkout
```

-------------------------------------------------------------------------------

## Socket Event

Client

```
lock-seats
```

Payload

```json
{
    "showId":12,

    "seatIds":[
        "A1",
        "A2"
    ],

    "userId":5
}
```

-------------------------------------------------------------------------------

## Backend Responsibilities

Receive

```
lock-seats
```

↓

Validate

Seats

↓

Lock Seats

↓

Broadcast

seat-locked

-------------------------------------------------------------------------------

## Checkout Route

```
/shows/{showId}/{state}/checkout
```

-------------------------------------------------------------------------------

# 5. BookingHistory.jsx

## Purpose

Displays all bookings of logged in user.

-------------------------------------------------------------------------------

## Backend API

```
GET /book
```

-------------------------------------------------------------------------------

## Expected Response

```json
{
    "bookings":[
        {

            "bookingRef":"BMS12345",

            "bookingDateTime":"2026-07-20",

            "paymentMethod":"razorpay",

            "bookingFee":{

                "ticketPrice":870,

                "convenience":43.5,

                "total":913.5
            },

            "showId":{

                "movie":{

                    "title":"F1",

                    "posterUrl":"...",

                    "format":[]
                },

                "theater":{

                    "name":"PVR",

                    "location":"Indore"
                },

                "date":"22-07-2026",

                "startTime":"10:30 AM"
            },

            "seats":[
                "A1",
                "A2"
            ]
        }
    ]
}
```

-------------------------------------------------------------------------------

## Fields Used

Booking

```
bookingRef

bookingDateTime

paymentMethod

bookingFee
```

Movie

```
title

posterUrl

format
```

Show

```
date

startTime
```

Theater

```
name

location
```

Seats

```
A1

A2

A3
```

-------------------------------------------------------------------------------

## Backend Recommendation

Sort

```
Newest First
```

Support

Pagination

later.

-------------------------------------------------------------------------------

# 6. Recommended.jsx

## Purpose

Displays recommended movies on Home page.

-------------------------------------------------------------------------------

## Backend API

```
GET /movies/recommended
```

-------------------------------------------------------------------------------

## Expected Response

```json
{

    "topMovies":[

        {

            "_id":8,

            "title":"F1",

            "posterUrl":"...",

            "rating":9.4,

            "votes":"12K",

            "genre":[]
        }

    ]
}
```

-------------------------------------------------------------------------------

## Navigation

Movie Click

↓

```
/movies/{state}/{movieName}/{movieId}/ticket
```

-------------------------------------------------------------------------------

## Backend Recommendation

Instead of hardcoded recommendation

Use

```
Highest Rated

Most Booked

Trending

Recently Released
```

-------------------------------------------------------------------------------

# 7. BannerSlider.jsx

## Purpose

Displays promotional banners.

-------------------------------------------------------------------------------

Current Status

```
Static Images
```

-------------------------------------------------------------------------------

Future API

```
GET /banners
```

-------------------------------------------------------------------------------

Suggested Banner DTO

```json
{

    "id":1,

    "image":"...",

    "redirectUrl":"/movies/8"
}
```

-------------------------------------------------------------------------------

# 8. LiveEvents.jsx

## Purpose

Displays live events.

-------------------------------------------------------------------------------

Current Status

```
Static
```

-------------------------------------------------------------------------------

Future API

```
GET /events
```

-------------------------------------------------------------------------------

Suggested Event DTO

```json
{

    "id":4,

    "title":"Standup Comedy",

    "poster":"..."
}
```

-------------------------------------------------------------------------------

# 9. Footer.jsx (Main Footer)

## Purpose

Purely informational.

Contains

```
Social Links

Copyright

Company Info
```

-------------------------------------------------------------------------------

Backend Requirement

None.

-------------------------------------------------------------------------------

# 10. FullScreenLoader.jsx

## Purpose

Displayed while application is loading.

Example

```
GET /users/me

↓

Loader

↓

Response

↓

Application
```

-------------------------------------------------------------------------------

Backend Impact

None.

-------------------------------------------------------------------------------

# 11. Seat.jsx

## Purpose

Old reusable seat component.

Currently

NOT USED

Final implementation exists inside

```
SeatLayout.jsx
```

Can safely ignore.

-------------------------------------------------------------------------------

# Complete Backend Response Models

-------------------------------------------------------------------------------

Movie

```java
Long id

String title

String description

String posterUrl

Double rating

String votes

List<String> genre

List<String> languages

List<String> format

String duration

LocalDate releaseDate

String certification

String age
```

-------------------------------------------------------------------------------

Theater

```java
Long id

String name

String logo

String city

String state

String location
```

-------------------------------------------------------------------------------

Show

```java
Long id

Movie movie

Theater theater

String date

String startTime

String audioType

List<Row> seatLayout
```

-------------------------------------------------------------------------------

Booking

```java
Long id

String bookingRef

LocalDateTime bookingDateTime

String paymentMethod

BookingFee bookingFee

Show show

List<String> seats
```

-------------------------------------------------------------------------------

BookingFee

```java
Double ticketPrice

Double convenience

Double total
```

-------------------------------------------------------------------------------

User

```java
Long id

String name

String email

String phone

Boolean activateUser
```

-------------------------------------------------------------------------------

# Remaining Future APIs

Movies

```
GET /movies/search

GET /movies/upcoming
```

-------------------------------------------------------------------------------

Events

```
GET /events
```

-------------------------------------------------------------------------------

Banner

```
GET /banners
```

-------------------------------------------------------------------------------

Profile

```
PUT /users/profile

PUT /users/email

PUT /users/phone
```

-------------------------------------------------------------------------------

# Final Backend Architecture

```
Authentication
│
├── Send OTP
├── Verify OTP
├── Activate User
├── Refresh Token
└── Logout

────────────────────────────

Users
│
├── Current User
├── Profile
└── Booking History

────────────────────────────

Movies
│
├── All Movies
├── Recommended
├── Search
├── Upcoming
└── Movie Details

────────────────────────────

Shows
│
├── Shows by Movie
├── Show Details
├── Seat Layout
└── Date Filter

────────────────────────────

Seats
│
├── Lock Seats
├── Unlock Seats
├── Seat Availability
└── WebSocket Updates

────────────────────────────

Payments
│
├── Create Razorpay Order
└── Verify Payment

────────────────────────────

Bookings
│
├── Create Booking
├── Booking History
└── Booking Fee Calculation

────────────────────────────

Utilities
│
├── Banner
├── Events
└── Search
```

-------------------------------------------------------------------------------

# Implementation Order (Recommended)

```
1. User Entity

↓

2. Movie Entity

↓

3. Theater Entity

↓

4. Show Entity

↓

5. Seat Layout

↓

6. Authentication

↓

7. Movie APIs

↓

8. Show APIs

↓

9. Seat Locking

↓

10. Razorpay

↓

11. Booking

↓

12. Booking History

↓

13. WebSocket

↓

14. Search

↓

15. Banner & Events
```

-------------------------------------------------------------------------------

## Backend Checklist

Authentication

☐ Send OTP

☐ Verify OTP

☐ Activate User

☐ Logout

☐ Refresh Token

-------------------------------------------------------------------------------

Movie

☐ List Movies

☐ Movie Details

☐ Recommended Movies

☐ Search

-------------------------------------------------------------------------------

Show

☐ Shows by Movie

☐ Show Details

☐ Date Filter

-------------------------------------------------------------------------------

Seat

☐ Seat Layout

☐ Lock Seats

☐ Unlock Seats

☐ Seat Expiry

-------------------------------------------------------------------------------

Payment

☐ Create Order

☐ Verify Payment

-------------------------------------------------------------------------------

Booking

☐ Create Booking

☐ Booking History

☐ Booking Reference Generator

☐ Payment Mapping