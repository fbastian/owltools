package owltools.sim2.preprocessor;

import org.semanticweb.owlapi.model.OWLObjectProperty;

import owltools.sim2.SimpleOwlSim.SimConfigurationProperty;

public class PropertyViewSimPreProcessor extends AbstractOBOSimPreProcessor {
	
	public OWLObjectProperty analysisRelation;
	
	public void preprocess() {
		
		LOG.info("Creating view from: "+analysisRelation);
		makeReflexive(analysisRelation);
		createPropertyView(analysisRelation,
				getOWLDataFactory().getOWLThing(), "%s related");
		trim();
	
	}
	
	public OWLObjectProperty getAboxProperty() {
		return analysisRelation;
	}

}
