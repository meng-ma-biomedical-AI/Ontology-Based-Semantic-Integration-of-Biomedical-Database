`{:description "This rule finds any modified ENSEMBL dna record described in Reactome.  The rule generates the Reactome dna's ICE identifier, and places the top-level entity on the BIO side as a localized subclass of the BIO entity denoted by the ENSEMBL ICE id.  It also makes the denotational link to a new ENSEMBL BIO entity if one doesn't exist already.",
 :name "step-hda-reactome_add-reactome-variant-dnas-to-bio",
 :reify ([?/dna_sc_variant {:ln (:sha-1 "bio-side modified reactome dna" ?/dna_record), :ns "kbio" :prefix "B_"}]
         [?/dna_sc {:ln (:sha-1 "bio-side reactome dna" ?/dna_record), :ns "kbio" :prefix "B_"}]),
 :head ((?/reactome_id obo/IAO_0000219 ?/dna_sc_variant)
         (?/dna_sc_variant rdfs/subClassOf ?/variant_restriction)
         (?/variant_restriction owl/onProperty ?/variant_of_bio)
         (?/variant_restriction owl/someValuesFrom ?/dna_sc)
         (?/variant_restriction rdf/type owl/Restriction)
         (?/dna_sc rdfs/subClassOf ?/dna)),
 :body "PREFIX franzOption_memoryLimit: <franz:85g>
PREFIX franzOption_memoryExhaustionWarningPercentage: <franz:95>
PREFIX franzOption_logQuery: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?reactome_id ?dna_id ?dna ?variant_of
WHERE {
      ?dna_record rdf:type ccp:IAO_EXT_0001556 .  # dna record

      OPTIONAL {
                ?dna_record obo:BFO_0000051 ?modification_feature_record .
                ?modification_feature_record rdf:type ccp:IAO_EXT_0001586 . #  modification features allowed
                }
      OPTIONAL {
                ?dna_record obo:BFO_0000051 ?fragment_feature_record .
                ?fragment_feature_record rdf:type ccp:IAO_EXT_0001587 .
                }
      filter (bound (?modification_feature_record) || bound (?fragment_feature_record)) .

      ?dna_record obo:BFO_0000051 ?react_xref_record .
      ?react_xref_record rdf:type ccp:IAO_EXT_0001588 . # xref field
      ?react_xref_record obo:BFO_0000051 ?react_id_field .
      ?react_id_field rdf:type ?reactome_id .
      ?reactome_id rdfs:subClassOf ccp:IAO_EXT_0001643 .

      ?dna_record obo:BFO_0000051 ?entity_record .
      ?entity_record rdf:type ccp:IAO_EXT_0001560 .  # dna reference record
      ?entity_record obo:BFO_0000051 ?xref_record .
      ?xref_record rdf:type ccp:IAO_EXT_0001588 . # xref field
      ?xref_record obo:BFO_0000051 ?dna_id_field .
      ?dna_id_field rdf:type ccp:IAO_EXT_0001968 . # Reactome DNA identifier field value
      ?dna_id_field rdf:type ?dna_id .
      ?dna_id obo:IAO_0000219 ?dna .


  {
   select ?variant_of {
                       <http://ccp.ucdenver.edu/kabob/ice/so#variant_of> obo:IAO_0000219 ?variant_of .
                               filter (?variant_of != <http://purl.obolibrary.org/obo/so#variant_of>) .
                       }
   }
   }",
 :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}
