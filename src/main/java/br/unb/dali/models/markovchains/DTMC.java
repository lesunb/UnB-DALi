package br.unb.dali.models.markovchains;

import agg.xt_basis.Graph;
import br.unb.dali.models.AModel;
import br.unb.dali.models.exceptions.ModelSemanticsVerificationException;

public class DTMC extends AModel {

	public DTMC(Graph graph) throws ModelSemanticsVerificationException {
		super(graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkModel() throws ModelSemanticsVerificationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setUp(Graph graph) {
		// TODO Auto-generated method stub

	}

}