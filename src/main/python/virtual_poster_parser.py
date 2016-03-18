import sys, csv, codecs
from utils import meetingTurtle, sessionTurtle, abstractTurtle
from person import Person

# command line inputs
vpCsvFile=sys.argv[1]
year=sys.argv[2]
ttlOutputFile=sys.argv[3]

meetingUris = []
sessionUris = []

ostream = codecs.open(ttlOutputFile,"a","utf8")

counter=-1
with open(vpCsvFile, 'rU') as csvfile:
	r = csv.reader(csvfile)
	headers = r.next()
	for fields in r:
		counter=counter+1
		id=fields[0]
		meetingTitle='Virtual Posters Session ' + id
		title=fields[1]
		abstract=fields[2].replace("\"","'")
		oDivision=fields[3]
		sessionID=''.join(c for c in fields[3] if c.isupper())
		division=fields[3].replace(" ","")

		authorFirstName1=fields[5]
		authorLastName1=fields[6]
		authorEmail1=fields[8]
		authorAffiliation1=fields[9]

		authorFirstName2=fields[16]
		authorLastName2=fields[17]
		authorEmail2=fields[19]
		authorAffiliation2=fields[20]

		authorFirstName3=fields[27]
		authorLastName3=fields[28]
		authorEmail3=fields[30]
		authorAffiliation3=fields[31]

		authorFirstName4=fields[38]
		authorLastName4=fields[39]
		authorEmail4=fields[41]
		authorAffiliation4=fields[42]

		authorFirstName5=fields[49]
		authorLastName5=fields[50]
		authorEmail5=fields[52]
		authorAffiliation5=fields[53]

		authorFirstName6=fields[60]
		authorLastName6=fields[61]
		authorEmail6=fields[63]
		authorAffiliation6=fields[64]

		authorFirstName7=fields[71]
		authorLastName7=fields[72]
		authorEmail7=fields[74]
		authorAffiliation7=fields[75]

		authorFirstName8=fields[82]
		authorLastName8=fields[83]
		authorEmail8=fields[85]
		authorAffiliation8=fields[86]

		authorFirstName9=fields[93]
		authorLastName9=fields[94]
		authorEmail9=fields[96]
		authorAffiliation9=fields[97]

		posterFileName=fields[103]

		# for each author create uris 
		authors = []
		tmpId = sessionID + '_' + str(counter)
		person1 = Person(authorFirstName1, authorLastName1, authorEmail1, authorAffiliation1, year, tmpId)
		authors.append(person1)
		if ( authorFirstName2 != '' ):
			person2 = Person(authorFirstName2, authorLastName2, authorEmail2, authorAffiliation2, year, tmpId)
			authors.append(person2)
		if ( authorFirstName3 != '' ):
			person3 = Person(authorFirstName3, authorLastName3, authorEmail3, authorAffiliation3, year, tmpId)
			authors.append(person3)
		if ( authorFirstName4 != '' ):
			person4 = Person(authorFirstName4, authorLastName4, authorEmail4, authorAffiliation4, year, tmpId)
			authors.append(person4)
		if ( authorFirstName5 != '' ):
			person5 = Person(authorFirstName5, authorLastName5, authorEmail5, authorAffiliation5, year, tmpId)
			authors.append(person5)
		if ( authorFirstName6 != '' ):
			person6 = Person(authorFirstName6, authorLastName6, authorEmail6, authorAffiliation6, year, tmpId)
			authors.append(person6)
		if ( authorFirstName7 != '' ):
			person7 = Person(authorFirstName7, authorLastName7, authorEmail7, authorAffiliation7, year, tmpId)
			authors.append(person7)
		if ( authorFirstName8 != '' ):
			person8 = Person(authorFirstName8, authorLastName8, authorEmail8, authorAffiliation8, year, tmpId)
			authors.append(person8)
		if ( authorFirstName9 != '' ):
			person9 = Person(authorFirstName9, authorLastName9, authorEmail9, authorAffiliation9, year, tmpId)
			authors.append(person9)

		# meeting
		meetingUri='http://abstractsearch.agu.org/meetings/' + year + '/VP/' + id
		if ( meetingUri not in meetingUris ):
			meetingUris.append(meetingUri)
			mTurtle = meetingTurtle(meetingUri, 'VP', meetingTitle, year) 
			ostream.write(unicode(mTurtle, errors="ignore"))

		# session
		sessionUri=meetingUri + '/' + division
		if ( sessionUri not in sessionUris ):
			sessionUris.append(sessionUri)
			sTurtle = sessionTurtle( sessionUri, division, sessionID, meetingUri )
			ostream.write(unicode(sTurtle, errors="ignore"))

		# poster/abstract
		posterUri=sessionUri + '/' + 'VP_' + str(counter)
		aTurtle = abstractTurtle( posterUri, sessionUri, title, abstract, authors )
		ostream.write(unicode(aTurtle, errors="ignore"))                