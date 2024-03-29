/* CVS $Id: $ */
package ca.uqam.vocabulary.model; 
import org.apache.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from /home/ubuntu/VIVO_UQAM_DEV/00-GIT/CCRD-CRDC/src/main/resources/00-crdc-ccdr-vocabulary/crdc-ccrd-semantic.ttl 
 * @author Auto-generated by schemagen on 13 Dec 2022 16:32 
 */
public class CRDC_CCRD {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static final Model M_MODEL = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://vivo.uqam.ca/vocabulary/crdc-ccrd#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = M_MODEL.createResource( NS );
    
    /** <p>The ontology's owl:versionInfo as a string</p> */
    public static final String VERSION_INFO = "Transformed in ontology by Université du Québec à Montréal (2021/07/21)";
    
    public static final Property classDefinition = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#classDefinition" );
    
    public static final Property classTitle = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#classTitle" );
    
    public static final Property elementDescription = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#elementDescription" );
    
    public static final Property elementLabel = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#elementLabel" );
    
    public static final Property elementTypeLabel = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#elementTypeLabel" );
    
    public static final Property hasHierarchicalStructure = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#hasHierarchicalStructure" );
    
    public static final Property hasID = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#hasID" );
    
    public static final Property hasLevel = M_MODEL.createProperty( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#hasLevel" );
    
    public static final Resource CRDC_CCRD_Class = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#CRDC_CCRD_Class" );
    
    public static final Resource CRDC_CCRD_Division = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#CRDC_CCRD_Division" );
    
    public static final Resource CRDC_CCRD_Group = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#CRDC_CCRD_Group" );
    
    public static final Resource CRDC_CCRD_SubClass = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#CRDC_CCRD_SubClass" );
    
    public static final Resource Category = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#Category" );
    
    public static final Resource Entity = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#Entity" );
    
    public static final Resource FOR_DDR_Entity = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#FOR_DDR_Entity" );
    
    public static final Resource SEO_OSE_Entity = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#SEO_OSE_Entity" );
    
    public static final Resource TOA_TDA_Entity = M_MODEL.createResource( "http://vivo.uqam.ca/vocabulary/crdc-ccrd#TOA_TDA_Entity" );
    
}
