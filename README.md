## Bin service

##### Introduction

The bin service provides bin (bank identification number) lookup information to other services in the gateway such as
transaction-service and gateway-risk-service.

Bin data includes information such as card issuer, country, card designation (personal or business) amongst other
things. Our data is provided by [binbase](https://binbase.net/) and this has a
[well-defined structure](https://www.binbase.com/structure.html)


##### Updating bin-service with new data from binbase

The bin service ships the bin base data files as part of its code as updates from binbase are provided infrequently
and usually there is one update a month. In order to roll a new version of the bin base data in to production you
will need to follow the procedure documented below.

1. Obtain the following files from the BIN base update email (this information is generally sent by Vivek)
    * `7-11 digit BIN database in semicolon delimited format - extended version`
    * `6 digit BIN database in semicolon delimited format - extended version (PERSONAL/COMMERCIAL and REGULATED/NON-REGULATED flags)`
1. Create a new branch from master (ensure that you have pulled latest master changes) using
`git checkout -b update_binbase_files`
1. Delete the existing bin base files that reside in bin-service/bin-service-persistence/src/main/resources
1. Unzip the files from a) and place in bin-service/bin-service-persistence/src/main/resources and ensure they have the names binbase_7-11.csv and binbase_extended.csv
1. Run BinBaseFileLoaderTest.shouldLoadProductionFilesWithoutFailure and upon verifying the total number of records loaded update the LATEST_RECORD_COUNT constant
1. Run a ./gradlew clean build to check for any test failures
1. Commit changes using commit message in the following format "Bin base <version> updates from <date of updates>"
1. Raise a PR to merge branch
1. Release new version of bin service

##### Lookup bin information in bulk

NI recently asked us to lookup the bin information for around 600 masked PAN numbers. As such a utility has been
created to allow these to be exported if the source bin/masked pan numbers are provided as a csv.

1. Get information from NI on the bins that need to be looked up. This should be provided as a csv file with a single
field per line which represents the bin. Note: Only the first 6 characters of this field will be used in bin lookup so
there is no need for NI to provide full PANs (nor should they given the sensitivity of this data)
2. Open up the MassBinLookupTest class and adjust the filename in lookupBinsAndOutputAsCsv to suit step 1.
3. Run the test and the output will provided in the same directory as the incoming file but this will be appended with
.lookedup.csv

