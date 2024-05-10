Feature: Query Bin information

  Background:
    Given I use version application/vnd.ni.bin-service.v1+json of the api
    Given I use mada file bin search
    Given I GET from uri /
    Then the response code is 200

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
      "bin" : "968201"
    }
    """
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected      |
      | bin            | isString | 968201        |
      | cardBrand      | is       | MADA          |
      | cardType       | is       | UNKNOWN       |
      | issuingOrg     | is       | Al Bilad Bank |
      | issuingCountry | is       | SA            |
      | coBadgedInfo   | is       | PREPAID       |

  Scenario: Should return card brand MADA when 520424 is searched
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "520424"
    }
    """
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected             |
      | bin            | isString | 520424               |
      | cardBrand      | is       | MADA                 |
      | cardType       | is       | UNKNOWN              |
      | issuingOrg     | is       | First Abu Dhabi Bank |
      | issuingCountry | is       | SA                   |
      | coBadgedInfo   | is       | Co-Badge MasterCard  |

  Scenario: Should find bin when searching with 11 digit pan using post
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "48185284589"
    }
    """
    Then the response code is 200
    And the response matches
      | path           | matcher  | expected                   |
      | bin            | isString | 481852                     |
      | cardBrand      | is       | MADA                       |
      | cardType       | is       | UNKNOWN                    |
      | issuingOrg     | is       | STC Pay ( non-bank issuer) |
      | issuingCountry | is       | SA                         |

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

  Scenario: Should fail when searching with bin that is too long using post
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "1234444444444444"
    }
    """
    Then the response code is 422
    And the response matches
      | path                         | matcher  | expected                            |
      | $.errors.size()              | isInt    | 1                                   |
      | $.errors[0].localizedMessage | contains | BIN must be numeric of size 8 to 11 |

  Scenario: Should find bin from binbase file if bin not present in mada file
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "446432"
    }
    """
    Then the response code is 200
    And the response matches
      | path               | matcher  | expected |
      | bin                | isString | 446432   |
      | cardBrand          | is       | VISA     |
      | cardType           | is       | CREDIT   |
      | cardCategory       | is       | GOLD     |
      | issuingCountryCode | is       | 500      |
      | issuingCountry     | is       | MS       |