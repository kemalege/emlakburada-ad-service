# Emlakburada Ad Service

## Running the Service

The Gateway Service runs on `http://localhost:8080`.

## API Endpoints

### Ads

#### Place a New Ad
**POST** `/api/v1/ads`

**Request:**
```json
{
    "userId": 1,
    "location": "Izmir",
    "title": "title",
    "price": 1000000,
    "details": "details",
    "category": "Emlak"
}
```

#### Change Ad Status
**POST** `/api/v1/ads/{id}/status`

**Request:**
```json
{
    "adStatus": "ACTIVE"
}
```

#### Get All Ads
**GET** `/api/v1/ads`

Retrieves a paginated list of ads. You can filter ads by their status.

**Query Parameters:**
- `adStatus` (optional): The status of the ads to retrieve (e.g., `ACTIVE`, `INACTIVE`).
- `size` (optional): The number of ads to return per page.
- `page` (optional): The page number to retrieve.

#### Delete an Ad
**DELETE** `/api/v1/ads/{id}/delete`

#### Update an Ad
**POST** `/api/v1/ads/{id}/update`

**Request:**
```json
{
    "userId": 1,
    "location": "location",
    "price": 25000000
}
```

#### Get Ad by Id
**GET** `/api/v1/ads/{id}`
