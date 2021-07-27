# Ontologie CCRD du web sémantique pour le web de données ouvertes et liées

## Ontologie de Classification canadienne de la recherche et développement pour le web de données liées et ouvertes

Cette ontologie est issue de la transformation des données ouvertes de format CSV du site 
de la [Classification canadienne de la recherche et développement (CCRD) 2020 version 1.0](https://www.statcan.gc.ca/fra/sujets/norme/ccrd/2020v1/indice)

### À propos du CCRD 2020 version 1.0

La Classification canadienne de la recherche et du développement (CCRD) 2020 a été élaborée 
conjointement par le Conseil de recherches en sciences humaines du Canada (CRSH), le Conseil 
de recherches en sciences naturelles et en génie du Canada (CRSNG), la Fondation canadienne pour 
l'innovation (FCI), les Instituts de recherche en santé du Canada (IRSC) et Statistique Canada 
qui en est le gardien. Cette classification-type conjointe, inspirée du modèle de Frascati 2015 
de l'Organisation de Coopération et de Développement Économiques (OCDE), sera utilisée par les 
organismes subventionnaires fédéraux et Statistique Canada pour recueillir et diffuser des données 
liées à la recherche et au développement au Canada. La première version officielle de la CCRD est 
la version 1.0 de 2020 qui est composée de 3 éléments principaux: 

1. le type d'activité ou TDA (avec 3 catégories), 
2. le domaine de recherche ou DDR (avec 1663 domaines au niveau le plus bas) 
3. l'objectif socioéconomique ou OSE (avec 85 groupes principaux au niveau le plus bas).

### Objectif de l'ontologie CRDC-CCRD

L'ontologie du CRDC-CCRD a pour objectif d'offrir une version bilingue (Français/Anglais) et interopérable pour sa diffusion sur le wed de données ouvertes liées du vocabulaire CRDC-CCRD.  

### Caractéristiques de l'ontologie CRDC-CCRD
L'ontologie du CRDC-CCRD possède les caractéristiques suivantes:

1. Le CRDC-CCRD est décrit avec le Resource Description Framework ([RDF]((https://www.w3.org/TR/rdf11-primer/)), son Schéma [RDF Schema 1.1](https://www.w3.org/TR/2014/REC-rdf-schema-20140225/) ainsi que [OWL 2 Web Ontology Language](https://www.w3.org/TR/owl2-primer/) qui sont des langages recommandés par le W3C
2. Le CRDC-CCRD est une symbolisation ontologique complète (classe, propriété, individu, 
classification et taxonomie) du contenu de l'ensemble des fichiers CSV du site du [CCRD (Français)](https://www.statcan.gc.ca/fra/sujets/norme/ccrd/2020v1/indice) et du [CRDC (Anglais)](https://www.statcan.gc.ca/eng/subjects/standard/crdc/2020v1/index)  
3. L'ensemble des trois sous-domaines du CRDC-CCRD `(TDA/TAO, DDR/FOR, OSE/SEO)` sont accessibles à partir d'un seul fichier (`crdc-ccrd.ttl`)
4. La sémantique de la classification des données du CRDC-CCRD est exprimée dans le fichier (`crdc-ccrd-semantic.ttl`)
5. Le format du CCRD-CRDC permet l'utilisation du langage de requêtes interopérables [SPARQL](https://www.w3.org/TR/sparql11-overview/) recommandé par le W3C
6. Une fois publié dans un serveur web de données, le CRDC-CCRD devient une composante que web de données ouvertes et liées

# 1 Informations Techniques de cette ontologie

## 1-1 Répertoires et Fichiers

### 1-1-1 Graphes RDF et classes Java

- [crdc-ccrd.ttl](crdc-ccrd.ttl) graphe-de-données représentant le vocabulaire du CRDC-CCRD de format RDF/RDFS/OWL dans la notation TURTLE. 
- [src/main/resources/00-crdc-ccdr-vocabulary/crdc-ccrd-semantic.ttl](src/main/resources/00-crdc-ccdr-vocabulary/crdc-ccrd-semantic.ttl) Cadre des données contenu dans le graphe
- [src/main/java/ca/uqam/vocabulary/crdc_ccrd/CrdcCcrd_VocabBuilder.java](src/main/java/ca/uqam/vocabulary/crdc_ccrd/CrdcCcrd_VocabBuilder.java) programme de conversion des fichiers CSV en graphe de données du CRDC-CCRD

### 1-1-2 Structure de répertoires 

- `/CCRD-CRDC/src/main/resources/csv` contiens les fichiers CSV extraits du site du CRDC-CCRD
- `/CCRD-CRDC/src/main/resources/01-datasets` représentation RDF/RDFS des fichiers CSV générés par `CrdcCcrd_VocabBuilder.java`
- `/CCRD-CRDC/src/main/resources/00-crdc-ccdr-vocabulary` Répertoire contenant l'ontologie structurant le graphe de données du CRDC-CCRD
- `/CCRD-CRDC/src/main/resources/doc` Documentation provenant du site du CRDC-CCRD
- `/CCRD-CRDC/src/main/java/ca/uqam/vocabulary/crdc_ccrd/` Répertoire contenant les classes Java nécessaires à la production des divers graphes
- `/CCRD-CRDC/src/main/java/ca/uqam/vocabulary/model/`Répertoire contenant les classes Java utiles à la gestion des IRI ontologiques


## 1-2 IRI ontologique

### 1-2-1 IRI du graphe de données du CRDC-CCRD

- Nom du fichier: `crdc-ccrd.ttl`
- IRI de Base: `http://purl.org/uqam.ca/vocabulary/crdc-ccrd/individual`
- Préfixe: `crdc-ccrd-data`

### 1-2-2 IRI de la sémantique du CRDC-CCRD

- Nom du fichier: `crdc-ccrd-semantic.ttl`
- II de Base: `http://purl.org/uqam.ca/vocabulary/crdc_ccrd`
- Préfixe: `crdc-ccrd`

### 1-2-3 IRI des fichiers transitoires représentant les données du CRDC-CCRD

- Nom du fichier: `crdc-ccrd-2020-{for-ddr|seo-ose|tao-tda}-element-{fra|eng}.ttl`
- IRI de Base: `http://ca.uqam/crdc-ccrd/csv`
- Préfixe: `crdc-ccrd-csv`

# 2 Nomenclature taxonomique des données du CRDC-CCRD

## 2-1 Le graphe crdc-ccrd.ttl

### 2-1-1 Les classes

Les données du CRDC-CCRD sont classifiées en quatre niveaux

- 1 Division
- 2 Groupe
- 3 Classe
- 4 Sous-classe

Dans le graphe `crdc-ccrd-data` chaque groupe de niveau supérieur est une super classe du niveau inférieur

exemple:

- pour :RDF20-21 de niveau 1
- pour :RDF201 de niveau 2
- pour :RDF20101 de niveau 3
- pour :RDF2010101 de niveau 4

on y retrouve la taxonomie suivante:

```
:RDF20-21
  rdf:type crdc_ccrd:CRDC_CCRD_Division ;
  rdf:type owl:Class ;
  rdfs:label "Génie et technologies"@fr-CA ;
  crdc_ccrd:hasLevel "1"^^xsd:double ;
  rdfs:subClassOf crdc_ccrd:FOR_DDR_Entity .

:RDF201
  rdf:type crdc_ccrd:CRDC_CCRD_Group ;
  rdf:type owl:Class ;
  crdc_ccrd:hasLevel "2"^^xsd:double ;
  rdfs:label "Génie civil, génie maritime, génie des transports, et génie minier"@fr-CA ;
  rdfs:subClassOf :RDF20-21 .

:RDF20101
  rdf:type crdc_ccrd:CRDC_CCRD_Class ;
  rdf:type owl:Class ;
  crdc_ccrd:hasLevel "3"^^xsd:double ;
  rdfs:label "Génie civil"@fr-CA ;
  rdfs:subClassOf :RDF201 .

:RDF2010101
  rdf:type crdc_ccrd:CRDC_CCRD_SubClass ;
  rdf:type owl:Class ;
  crdc_ccrd:hasLevel "4"^^xsd:double ;
  rdfs:label "Géotechnique"@fr-CA ;
  rdfs:subClassOf :RDF20101 .

``` 

### 2-1-2 Les individus ontologiques

Chaque niveau de classes possède des éléments associés. Les éléments sont associés à la classe par le prédicat `rdf:type`

exemple:

L'Élément `RDF1030507.183 (semiconducteurs composés)` est de type `RDF1030507 (Supraconductivité)` et ci-dessous, la représentation synthétisée en notation TURTLE

```
:RDF1030507.183
  rdf:type :RDF1030507 ;
  rdf:type owl:NamedIndividual ;
  rdfs:label "compound semiconductors (See RDF2060103 Compound semiconductors)"@en-CA ;
  rdfs:label "semiconducteurs composés (Voir RDF2060103 Semi-conducteurs composés)"@fr-CA .

:RDF1030507
  rdf:type owl:Class ;
  rdf:type crdc_ccrd:CRDC_CDDR_SubClass ;
  crdc_ccrd:hasLevel "4"^^xsd:double ;
  rdfs:label "Superconductivity"@en-CA ;
  rdfs:label "Supraconductivité"@fr-CA ;
  rdfs:subClassOf :RDF10305 .

```

# 3 Utilisation

Le graphe `ccrd-crdc.ttl` peut être utilisé seul. Cependant, il est préférable de l'utiliser avec `crdc-ccrd-semantic.ttl` puisqu'il est importé par `ccrd-crdc.ttl`

# 4 Procédure de création de ccrd-crdc.ttl

- L'exécution de cette procédure n'est pas obligatoire puisque le fichier `ccrd-crdc.ttl` est déjà généré.

#### A) Importation des fichiers CSV à partir du site du 
[CRDC-CCRD](https://www.statcan.gc.ca/eng/subjects/standard/crdc/2020v1/index)

dans le répertoire [src/main/resources/csv](src/main/resources/csv) importer les fichiers suivants:

- wget https://www.statcan.gc.ca/eng/statistical-programs/document/crdc-ccrd-2020-toa-tda-structure-eng.csv
- wget https://www.statcan.gc.ca/eng/statistical-programs/document/crdc-ccrd-2020-for-ddr-structure-eng.csv
- wget https://www.statcan.gc.ca/eng/statistical-programs/document/crdc-ccrd-2020-for-ddr-element-eng.csv
- wget https://www.statcan.gc.ca/eng/statistical-programs/document/crdc-ccrd-2020-seo-ose-structure-eng.csv
- wget https://www.statcan.gc.ca/eng/statistical-programs/document/crdc-ccrd-2020-seo-ose-element-eng.csv
- wget https://www.statcan.gc.ca/fra/programmes-statistiques/document/crdc-ccrd-2020-toa-tda-structure-fra.csv
- wget https://www.statcan.gc.ca/fra/programmes-statistiques/document/crdc-ccrd-2020-for-ddr-structure-fra.csv
- wget https://www.statcan.gc.ca/fra/programmes-statistiques/document/crdc-ccrd-2020-for-ddr-element-fra.csv
- wget https://www.statcan.gc.ca/fra/programmes-statistiques/document/crdc-ccrd-2020-seo-ose-structure-fra.csv
- wget https://www.statcan.gc.ca/fra/programmes-statistiques/document/crdc-ccrd-2020-seo-ose-element-fra.csv

Pour chaque première ligne (les titres) de chaque fichier csv: supprimez les espaces par le caractère '_' et remplacer les caractères spéciaux (ex.: 'é' ) par des caractères universels ('e')
exemple pour le fichier `crdc-ccrd-2020-for-ddr-element-fra.csv`

remplacer :

```
Niveau,Code,Titres de classes,Nom du type d’élément Français,Description d’élément Français

```
par

```
﻿Niveau,Code,Titres_de_classes,Nom_du_type_d_element_Francais,Description_d_element_Francais
```


#### B) Générer le fichier `ccrd-crdc.ttl` en exécutant la commande: 

`mvn -q clean test -Pbuild-ccrd-crdc`

* Note: Assurez-vous d'avoir installé le java-JDK 1.8 ainsi que l'utilitaire Maven

-- FIN ---
　

　
