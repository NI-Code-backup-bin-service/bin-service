Feature: Production-ready features
  Background:
    Given I use version application/vnd.ni.bin-service.v1+json of the api

  Scenario: Report application information
    When I GET from uri /actuator/info
    Then the response code is 200
    And the response matches
      | path              | matcher | expected         |
      | $.app.name        | is      | bin-service      |
      | $.app.description | is      | NI Micro Service |


  Scenario: Prometheus metrics endpoint
    When I GET mediaType text/plain from uri /actuator/prometheus
    Then the response code is 200
    And text body contains lines matching
      | jvm_[^\\{]+\\{application=\"bin-service\",.*\\} \\d+\\.\\d+     |
      | logback_[^\\{]+\\{application=\"bin-service\",.*\\} \\d+\\.\\d+ |
      | process_[^\\{]+\\{application=\"bin-service\",.*\\} \\d+\\.\\d+ |
      | system_[^\\{]+\\{application=\"bin-service\",.*\\} \\d+\\.\\d+  |


  Scenario: Health check is healthy
    When I GET from uri /actuator/health
    Then the response code is 200
    And the response matches
      | path     | matcher | expected |
      | $.status | is      | UP       |
    When I GET mediaType text/plain from uri /actuator/prometheus
    Then the response code is 200
    And text body contains lines matching
      | spring_actuator_health\\{application=\"bin-service\",.*\\} 2\\.0 |


  Scenario: Should query bin and generate metrics
    Given I GET from uri /
    Then the response code is 200
    When I POST to link rel 'ni:bin-search' with body
    """
    {
      "bin" : "40746100"
    }
    """
    Then the response code is 200
    When I GET mediaType text/plain from uri /actuator/prometheus
    Then the response code is 200
    And text body contains lines matching
      | http_server_requests_seconds_count\\{application=\"bin-service\",exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/bin\",\\} \\d\\.0                                                |
      | http_server_requests_seconds_sum\\{application=\"bin-service\",exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/bin\",\\} \\d+\\.\\d+                                              |
      | http_server_requests_seconds_max\\{application=\"bin-service\",exception=\"None\",method=\"POST\",outcome=\"SUCCESS\",status=\"200\",uri=\"/bin\",\\} \\d+\\.\\d+                                              |
