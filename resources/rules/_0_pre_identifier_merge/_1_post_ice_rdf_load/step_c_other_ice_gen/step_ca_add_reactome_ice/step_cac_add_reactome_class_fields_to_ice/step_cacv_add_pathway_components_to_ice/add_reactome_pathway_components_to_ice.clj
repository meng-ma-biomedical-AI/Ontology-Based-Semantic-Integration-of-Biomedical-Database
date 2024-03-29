`{:description "This rule finds any pathway component field described in Reactome; these connect pathways and their pathway steps.",
 :name "add_reactome_pathway_components_to_ice",
  :reify ([?/this_pathway_component_record {:ln (:sha-1 ?/pathway_component_record ?/pathway_record), :ns "http://ccp.ucdenver.edu/kabob/ice/" :prefix "R_"}]),
  :head ((?/pathway_record obo/BFO_0000051 ?/this_pathway_component_record)
         (?/this_pathway_component_record rdfs/subClassOf ?/pathway_component_record)
        
        (?/this_pathway_component_record rdf/type ccp/IAO_EXT_0001541) ;; pathway component field
        ),
 :body "#add_reactome_pathway_components_to_ice.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX kice: <http://ccp.ucdenver.edu/kabob/ice/>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?pathway_record ?pathway_component_record WHERE {
 ?pathway bp:pathwayComponent ?pathway_component .
 ?pathway ccp:ekws_temp_biopax_connector_relation ?pathway_record .
 ?pathway_component ccp:ekws_temp_biopax_connector_relation ?pathway_component_record .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}




         

         
