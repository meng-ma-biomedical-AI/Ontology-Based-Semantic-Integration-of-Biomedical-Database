`{:description "This rule finds any next step field in a pathway step record described in Reactome.",
 :name "add_reactome_next_steps_to_ice",
 :reify ([?/this_next_step_record {:ln (:sha-1 ?/next_step_record ?/pathway_step_record), :ns "http://ccp.ucdenver.edu/kabob/ice/" :prefix "R_"}]),
 :head ((?/pathway_step_record obo/BFO_0000051 ?/this_next_step_record)
        (?/this_next_step_record rdfs/subClassOf ?/next_step_record)
        (?/this_next_step_record rdf/type ccp/IAO_EXT_0001943) ;; next step field
        ),
 :body "#add_reactome_next_steps_to_ice.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX kice: <http://ccp.ucdenver.edu/kabob/ice/>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?pathway_step_record ?next_step_record WHERE {
 ?pathway_step bp:nextStep ?next_step .
 ?pathway_step ccp:ekws_temp_biopax_connector_relation ?pathway_step_record .
 ?next_step ccp:ekws_temp_biopax_connector_relation ?next_step_record .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}




         

         
