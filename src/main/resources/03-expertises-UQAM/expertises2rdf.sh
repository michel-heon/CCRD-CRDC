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
export LOC_SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd -P)"
source $LOC_SCRIPT_DIR/00-env.sh
source $BASH_LIB/logging.sh
source $BASH_LIB/cleanup.sh
export verbosity=4 # niveau de verbosité de logger : 4=info
###################################################################
## Le noyau du script
###################################################################
LOOP_CTR=0
rm $EXPERT_UQAM_FR_ONTO
cat expertises_list.txt | while read line ; do
    ((LOOP_CTR=LOOP_CTR+1))
    LABEL=${line}
    info "$LOOP_CRT  $LABEL"
    cat << EOF >> $EXPERT_UQAM_FR_ONTO
    <http://uqam.ca/ontology/expertises#ln_$LOOP_CTR> <http://www.w3.org/2000/01/rdf-schema#label> "$LABEL"@FR-CA .
    <http://uqam.ca/ontology/expertises#ln_$LOOP_CTR> <http://www.w3.org/2004/02/skos/core#prefLabel> "$LABEL"@FR-CA .
    <http://uqam.ca/ontology/expertises#ln_$LOOP_CTR> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2008/05/skos-xl#Label> .
EOF
done
