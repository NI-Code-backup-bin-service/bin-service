#!/usr/bin/env bash

set -e

if ((BASH_VERSINFO[0] < 4))
then
    echo "Sorry, you need at least bash-4.0 to run this script."
    echo "On mac try: brew install bash"
    exit 1
fi

source <(curl -s https://raw.githubusercontent.com/microdc/bash-libs/0.0.1/common.sh)
source <(curl -s https://raw.githubusercontent.com/microdc/bash-libs/0.0.3/docker_build.sh)


main() {
  check_dependency docker
  check_dependency jq

  export OWNER='network-international'
  export REPO='bin-service'
  export DOCKER_REPO=${1:-"${OWNER}/${REPO}"}
  export VERSION=${2:-unspecified}

  default_build "${DOCKER_REPO}" "${VERSION}" "${DOCKER_REPO%%/*}"

}

main "$@"
