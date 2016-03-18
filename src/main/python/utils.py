import hashlib

def meetingTurtle( meetingUri, meetingCode, meetingTitle, year ):
	triple1 = "<" + meetingUri + "> <http://abstractsearch.agu.org/ontology#meetingCode> \"" + meetingCode + "\"^^<http://www.w3.org/2001/XMLSchema#string> ."
	triple2 = "<" + meetingUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swrc.ontoware.org/ontology#Meeting> ."
	triple3 = "<" + meetingUri + "> <http://swrc.ontoware.org/ontology#eventTitle> \"" + meetingTitle + "\"@en ."
	triple4 = "<" + meetingUri + "> <http://swrc.ontoware.org/ontology#year> \"" + year + "\"^^<http://www.w3.org/2001/XMLSchema#gYear> ."
	line = triple1 + "\n" + triple2 + "\n" + triple3 + "\n" + triple4 + "\n"
	return line

def sessionTurtle( sessionUri, sessionTitle, sessionIdentifier, meetingUri ):
	triple1 = "<" + sessionUri + "> <http://swrc.ontoware.org/ontology#eventTitle> \"" + sessionTitle + "\"@en ."
	triple2 = "<" + sessionUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://data.semanticweb.org/ns/swc/ontology#SessionEvent> ."
	triple3 = "<" + sessionUri + "> <http://purl.org/dc/terms/identifier> \"" + sessionIdentifier + "\" ."
	triple4 = "<" + sessionUri + "> <http://data.semanticweb.org/ns/swc/ontology#isSubEventOf> <" + meetingUri + "> ."
	line = triple1 + "\n" + triple2 + "\n" + triple3 + "\n" + triple4 + "\n"
	return line

def abstractTurtle( abstractUri, sessionUri, abstractTitle, abstractText, authors ):
	triple1 = "<" + abstractUri + "> <http://data.semanticweb.org/ns/swc/ontology#relatedToEvent> <" + sessionUri + "> ."
	triple2 = "<" + abstractUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://abstractsearch.agu.org/ontology#Abstract> ."
	triple3 = "<" + abstractUri + "> <http://purl.org/dc/terms/title> \"" + abstractTitle + "\"@en ."
	triple4 = "<" + abstractUri + "> <http://abstractsearch.agu.org/ontology#raw> \"" + abstractText + "\"@en ."
	triple5 = "<" + abstractUri + "> <http://swrc.ontoware.org/ontology#abstract> \"" + abstractText + "\"@en ."

	count=1
	authorTurtle = []
	for author in authors:
		if count == 1:
			boolean = "true"
		else:
			boolean = "false"
	 	authorTurtle.append("<" + abstractUri + "> <http://tw.rpi.edu/schema/hasAgentWithRole> <" + author.authorUri + "> .")
	   	authorTurtle.append("<" + author.authorUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://tw.rpi.edu/schema/Author> .")
	   	authorTurtle.append("<" + author.authorUri + "> <http://abstractsearch.agu.org/ontology#isCorrespondingAuthor> \"" + boolean + "\"^^<http://www.w3.org/2001/XMLSchema#boolean> .")
		authorTurtle.append("<" + author.authorUri + "> <http://tw.rpi.edu/schema/index> \"" + str(count) + "\"^^<http://www.w3.org/2001/XMLSchema#positiveInteger> .")
		authorTurtle.append("<" + author.authorUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . ")
		authorTurtle.append("<" + author.authorUri + "> <http://xmlns.com/foaf/0.1/name> \"" + author.lastName + "," + author.firstName + "\"^^<http://www.w3.org/2001/XMLSchema#string> .")
	 	authorTurtle.append("<" + author.authorUri + "> <http://xmlns.com/foaf/0.1/mbox> \"" + author.email + "\"^^<http://www.w3.org/2001/XMLSchema#string> .")
		authorTurtle.append("<" + author.authorUri + "> <http://tw.rpi.edu/schema/withAffiliation> <" + author.affiliationUri + "> .")
		authorTurtle.append("<" + author.affiliationUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Organization> .")
		authorTurtle.append("<" + author.affiliationUri + "> <http://purl.org/dc/terms/description> \"" + author.affiliation + "\"^^<http://www.w3.org/2001/XMLSchema#string> .")
		authorTurtle.append("<" + author.affiliationUri + "> <http://abstractsearch.agu.org/ontology#organizationHash> <urn:x-org:" + hashlib.sha224(author.affiliation).hexdigest() + "> .")
		count = count + 1
	line = triple1 + "\n" + triple2 + "\n" + triple3 + "\n" + triple4 + "\n" + triple5 + "\n"
	for entry in authorTurtle:
		line = line + entry + "\n"
	return line