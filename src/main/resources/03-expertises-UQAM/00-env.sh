#!/bin/bash 

###################################################################
# Script Name   :
# Description   :
# Args          : 
# Author        : Michel Héon PhD
# Institution   : Université du Québec à Montréal
# Copyright     : Université du Québec à Montréal (c) 2023
# Email         : heon.michel@uqam.ca
###################################################################

###################################################################
## entête
###################################################################
export ENV_SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd -P)"
VIVO_STUDIO_PKG=$(realpath $ENV_SCRIPT_DIR/../../../../../vivo-studio)
source $VIVO_STUDIO_PKG/conf/vs-conf.sh
export EXPERT_UQAM_FR_ONTO=expertises_uqam_fr_ca.nt
export EXPERT_UQAM_EN_ONTO=expertises_uqam_en_ca.nt



