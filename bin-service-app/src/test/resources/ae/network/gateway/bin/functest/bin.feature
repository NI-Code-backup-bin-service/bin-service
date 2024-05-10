Feature: Query Bin information

  Background:
    Given I use version application/vnd.ni.bin-service.v1+json of the api
    Given I GET from uri /
    Then the response code is 200

  Scenario: Should query bin
    When I GET from templated link rel 'ni:bin' with parameters '407461'
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected |
      | bin            | isString | 407461  |
      | cardBrand      | is       | VISA     |
      | cardType       | is       | CREDIT   |
      | issuingCountry | isString | FR       |
      | personalCard   | isBool   | false    |
      | countryInGcc   | isBool   | false    |

  Scenario: Should query test bin
    When I GET from templated link rel 'ni:bin' with parameters '520424'
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected   |
      | bin            | isString | 520424     |
      | cardBrand      | is       | MASTERCARD |
      | cardType       | is       | CREDIT     |
      | issuingCountry | isString | US         |
      | personalCard   | isBool   | true       |
      | countryInGcc   | isBool   | false      |

  Scenario: Should not find unknown bin
    When I GET from templated link rel 'ni:bin' with parameters '100000'
    Then the response code is 404

  Scenario: Should fail with bin that is too short
    When I GET from templated link rel 'ni:bin' with parameters '123'
    Then the response code is 422

  Scenario: Should not find unknown bin when searching
    When I GET from templated link rel 'ni:bin-search' with parameters '100000'
    Then the response code is 204

  Scenario: Should find bin when searching
    When I GET from templated link rel 'ni:bin-search' with parameters '407461'
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected |
      | bin            | isString | 407461  |
      | cardBrand      | is       | VISA     |
      | cardType       | is       | CREDIT   |
      | issuingCountry | isString | FR       |
      | personalCard   | isBool   | false    |
      | countryInGcc   | isBool   | false    |

  Scenario: Should fail when searching with bin that is too short
    When I GET from templated link rel 'ni:bin-search' with parameters '123'
    Then the response code is 422

  Scenario: Should not find unknown bin when searching using post
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "100000"
    }
    """
    Then the response code is 204

  Scenario: Should find bin when searching using post
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "40746100"
    }
    """
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected |
      | bin            | isString | 407461  |
      | cardBrand      | is       | VISA     |
      | cardType       | is       | CREDIT   |
      | issuingCountry | isString | FR       |
      | personalCard   | isBool   | false    |
      | countryInGcc   | isBool   | false    |

  Scenario: Should find bin when searching using Apple Pay DAN in post request
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "44530800215"
    }
    """
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected  |
      | bin            | isString | 445308002 |
      | cardBrand      | is       | VISA      |
      | cardType       | is       | DEBIT     |
      | issuingCountry | isString | SA        |
      | personalCard   | isBool   | true      |
      | countryInGcc   | isBool   | true      |

  Scenario: Should fail when searching with bin that is too short using post
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "123"
    }
    """
    Then the response code is 422
    And the response matches
      | path                         | matcher  | expected                            |
      | $.errors.size()              | isInt    | 1                                   |
      | $.errors[0].localizedMessage | contains | BIN must be numeric of size 8 to 11 |
