package owltools.sim2;

import java.io.IOException;

import org.apache.commons.math.MathException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.obolibrary.oboformat.parser.OBOFormatParserException;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import owltools.graph.OWLGraphWrapper;
import owltools.io.OWLPrettyPrinter;
import owltools.io.ParserWrapper;

/**
 * This is the main test class for PropertyViewOntologyBuilder
 * 
 * @author cjm
 *
 */
public class BasicOWLSimTest extends AbstractOWLSimTest {

	private Logger LOG = Logger.getLogger(BasicOWLSimTest.class);
	
	@Test
	public void testBasicSim() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, MathException {
		ParserWrapper pw = new ParserWrapper();
		sourceOntol = pw.parseOWL(getResourceIRIString("sim/mp-subset-1.obo"));
		g =  new OWLGraphWrapper(sourceOntol);
		parseAssociations(getResource("sim/mgi-gene2mp-subset-1.tbl"), g);

		owlpp = new OWLPrettyPrinter(g);

		// assume buffering
		OWLReasoner reasoner = new ElkReasonerFactory().createReasoner(sourceOntol);
		try {

			sos = new SimpleOwlSim(sourceOntol);
			sos.setReasoner(reasoner);
			LOG.info("Reasoner="+sos.getReasoner());

			//sos.saveOntology("/tmp/z.owl");

			reasoner.flush();
			for (OWLNamedIndividual i : sourceOntol.getIndividualsInSignature()) {
				//System.out.println("COMPARING: "+i);
				for (OWLNamedIndividual j : sourceOntol.getIndividualsInSignature()) {
					showSim(i,j);
				}
			}
		}
		finally {
			reasoner.dispose();
		}
	}

	@Test 
	public void testGetEntropy() throws OWLOntologyCreationException, IOException, OBOFormatParserException {
		ParserWrapper pw = new ParserWrapper();
		sourceOntol = pw.parseOBO(getResourceIRIString("sim/mp-subset-1.obo"));
		g =  new OWLGraphWrapper(sourceOntol);
		parseAssociations(getResource("sim/mgi-gene2mp-subset-1.tbl"), g);

		owlpp = new OWLPrettyPrinter(g);

		// assume buffering
		OWLReasoner reasoner = new ElkReasonerFactory().createReasoner(sourceOntol);
		try {

			sos = new SimpleOwlSim(sourceOntol);
			sos.setReasoner(reasoner);

			reasoner.flush();
			Double e = sos.getEntropy();
			LOG.info("ENTROPY OF ONTOLOGY = "+e);

			for (String subset : g.getAllUsedSubsets()) {
				LOG.info("SUBSET:"+subset);
				e = sos.getEntropy(g.getOWLClassesInSubset(subset));
				LOG.info(" ENTROPY OF "+subset+" = "+e);
			}
		}
		finally {
			reasoner.dispose();
		}		
	}





}
