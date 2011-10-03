package org.agu.essi.data;

import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.SourceNotReadyException;

public class SparqlDataSource implements DataSource {

	public Vector<Abstract> getAbstracts() throws SourceNotReadyException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean ready() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setEntityMatcher(EntityMatcher m) {
		// TODO Auto-generated method stub

	}

	public EntityMatcher getEntityMatcher() {
		// TODO Auto-generated method stub
		return null;
	}

}
