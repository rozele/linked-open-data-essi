class Person(object):

    def __init__(self, firstName=None, lastName=None, email=None, affiliation=None, year=None, id=None):
        self.firstName = firstName.replace(" ", "")
        self.lastName = lastName.replace(" ","")
        self.email = email
        self.affiliation = affiliation
        self.authorUri = self.createAuthorUri(year, id)
        self.affiliationUri = self.createAffiliationUri(year, id)

    def createAuthorUri(self, year, id):
		uri = 'http://abstractsearch.agu.org/meetings/' + year + '/VP/Author_' + id + '_' + self.firstName + '_' + self.lastName
		return uri

    def createAffiliationUri(self, year, id):
		uri = 'http://abstractsearch.agu.org/meetings/' + year + '/VP/Affiliation_' + id + '_' + self.firstName + '_' + self.lastName
		return uri