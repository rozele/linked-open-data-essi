package essi.lod.util;

import essi.lod.data.agu.Pre2000Data;
import essi.lod.data.agu.Pre2000Session;
import essi.lod.entity.agu.Author;
import java.util.Vector;
import essi.lod.util.FileWrite;

public class OutputTurtlePeople
{
	
	public void write ( String outputFileName, String year, Vector <Pre2000Data> abstracts)
	{
		
		FileWrite fw = new FileWrite();
		
		for ( int i=0; i<abstracts.size(); i++ ) {
			
		   Pre2000Data d = abstracts.get(i);
		   Vector <Author> authors = d.getAuthors();
		   Author author = authors.get(0);
		   
		}
		   /*
		<http://abstracts.agu.org/people/simon.ricard@mddep.gouv.qc.ca> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/H/sessions/H33I/abstracts/H33I-1457/authors/3> .
			<http://abstracts.agu.org/people/simon.ricard@mddep.gouv.qc.ca> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/simon.ricard@mddep.gouv.qc.ca> <http://xmlns.com/foaf/0.1/name> "Ricard, S"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/simon.ricard@mddep.gouv.qc.ca> <http://xmlns.com/foaf/0.1/mbox> "simon.ricard@mddep.gouv.qc.ca"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/SM04_H53C_convener_R___Lawford> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2004/SM/sections/H/sessions/H53C/conveners/2> .
			<http://abstracts.agu.org/people/SM04_H53C_convener_R___Lawford> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/SM04_H53C_convener_R___Lawford> <http://xmlns.com/foaf/0.1/name> "R   Lawford"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/ronan@irfu.se> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2006/FM/sections/P/sessions/P13A/abstracts/P13A-0145/authors/3> .
			<http://abstracts.agu.org/people/ronan@irfu.se> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2006/FM/sections/P/sessions/P41A/abstracts/P41A-1257/authors/5> .
			<http://abstracts.agu.org/people/ronan@irfu.se> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/ronan@irfu.se> <http://xmlns.com/foaf/0.1/name> "Modolo, R"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/ronan@irfu.se> <http://xmlns.com/foaf/0.1/mbox> "ronan@irfu.se"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/vinscor@hotmail.com> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/FM/sections/A/sessions/A21E/abstracts/A21E-10/authors/10> .
			<http://abstracts.agu.org/people/vinscor@hotmail.com> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/FM/sections/A/sessions/A71B/abstracts/A71B-0107/authors/10> .
			<http://abstracts.agu.org/people/vinscor@hotmail.com> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/vinscor@hotmail.com> <http://xmlns.com/foaf/0.1/name> "Corbin, V"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/vinscor@hotmail.com> <http://xmlns.com/foaf/0.1/mbox> "vinscor@hotmail.com"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/llm840924@126.com> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/T/sessions/T53B/abstracts/T53B-08/authors/3> .
			<http://abstracts.agu.org/people/llm840924@126.com> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/llm840924@126.com> <http://xmlns.com/foaf/0.1/name> "Li, L"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/llm840924@126.com> <http://xmlns.com/foaf/0.1/mbox> "llm840924@126.com"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM08_U13A-0040_author_McNab,_M_C> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2008/FM/sections/U/sessions/U13A/abstracts/U13A-0040/authors/8> .
			<http://abstracts.agu.org/people/FM08_U13A-0040_author_McNab,_M_C> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM08_U13A-0040_author_McNab,_M_C> <http://xmlns.com/foaf/0.1/name> "McNab, M C"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2007/FM/sections/A/sessions/A53A/abstracts/A53A-0911/authors/2> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2006/FM/sections/A/sessions/A11A/abstracts/A11A-0824/authors/2> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/A/sessions/A53D/abstracts/A53D-04/authors/3> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/B/sessions/B51J/abstracts/B51J-02/authors/18> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/A/sessions/A33B/abstracts/A33B-0156/authors/6> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/A/sessions/A41F/abstracts/A41F-0177/authors/1> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://xmlns.com/foaf/0.1/name> "Zhong, S"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/zhongs@msu.edu> <http://xmlns.com/foaf/0.1/mbox> "zhongs@msu.edu"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/gga22@keele.ac.uk> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/T/sessions/T53C/abstracts/T53C-2731/authors/3> .
			<http://abstracts.agu.org/people/gga22@keele.ac.uk> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/gga22@keele.ac.uk> <http://xmlns.com/foaf/0.1/name> "Clarke, S"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/gga22@keele.ac.uk> <http://xmlns.com/foaf/0.1/mbox> "gga22@keele.ac.uk"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/lmack@rdc.uscg.mil> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2000/OS/sections/OS/sessions/OS22B/abstracts/OS22B-14/authors/1> .
			<http://abstracts.agu.org/people/lmack@rdc.uscg.mil> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/lmack@rdc.uscg.mil> <http://xmlns.com/foaf/0.1/name> "Mack, L"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/lmack@rdc.uscg.mil> <http://xmlns.com/foaf/0.1/mbox> "lmack@rdc.uscg.mil"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM11_H53L-06_author_The_ParFlow_development_team> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/H/sessions/H53L/abstracts/H53L-06/authors/6> .
			<http://abstracts.agu.org/people/FM11_H53L-06_author_The_ParFlow_development_team> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM11_H53L-06_author_The_ParFlow_development_team> <http://xmlns.com/foaf/0.1/name> "The ParFlow development team"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM05_MR12A_convener_C_B_Agee> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2005/FM/sections/MR/sessions/MR12A/conveners/1> .
			<http://abstracts.agu.org/people/FM05_MR12A_convener_C_B_Agee> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM05_MR12A_convener_C_B_Agee> <http://xmlns.com/foaf/0.1/name> "C B Agee"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM12_C31D_convener_J_S_Deems> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/C/sessions/C31D/conveners/2> .
			<http://abstracts.agu.org/people/FM12_C31D_convener_J_S_Deems> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM12_C31D_convener_J_S_Deems> <http://xmlns.com/foaf/0.1/name> "J S Deems"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/cmoon2@ucmerced.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2009/FM/sections/GC/sessions/GC51A/abstracts/GC51A-0729/authors/5> .
			<http://abstracts.agu.org/people/cmoon2@ucmerced.edu> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/cmoon2@ucmerced.edu> <http://xmlns.com/foaf/0.1/name> "Moon, C"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/cmoon2@ucmerced.edu> <http://xmlns.com/foaf/0.1/mbox> "cmoon2@ucmerced.edu"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM04_ED33B-0773_author_McManus,_J> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2004/FM/sections/ED/sessions/ED33B/abstracts/ED33B-0773/authors/8> .
			<http://abstracts.agu.org/people/FM04_ED33B-0773_author_McManus,_J> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM04_ED33B-0773_author_McManus,_J> <http://xmlns.com/foaf/0.1/name> "McManus, J"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM03_V51E-0325_author_Giordano,_D> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2003/FM/sections/V/sessions/V51E/abstracts/V51E-0325/authors/3> .
			<http://abstracts.agu.org/people/FM03_V51E-0325_author_Giordano,_D> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM03_V51E-0325_author_Giordano,_D> <http://xmlns.com/foaf/0.1/name> "Giordano, D"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/d.tatham@earth.leeds.ac.uk> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2005/FM/sections/V/sessions/V21A/abstracts/V21A-0593/authors/2> .
			<http://abstracts.agu.org/people/d.tatham@earth.leeds.ac.uk> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2006/FM/sections/T/sessions/T53C/abstracts/T53C-1617/authors/1> .
			<http://abstracts.agu.org/people/d.tatham@earth.leeds.ac.uk> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/d.tatham@earth.leeds.ac.uk> <http://xmlns.com/foaf/0.1/name> "Tatham, D"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/d.tatham@earth.leeds.ac.uk> <http://xmlns.com/foaf/0.1/mbox> "d.tatham@earth.leeds.ac.uk"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM12_NG32A_convener_V_G_Kossobokov> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/NG/sessions/NG32A/conveners/3> .
			<http://abstracts.agu.org/people/FM12_NG32A_convener_V_G_Kossobokov> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM12_NG32A_convener_V_G_Kossobokov> <http://xmlns.com/foaf/0.1/name> "V G Kossobokov"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/mfork@fiu.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/B/sessions/B51B/abstracts/B51B-0353/authors/1> .
			<http://abstracts.agu.org/people/mfork@fiu.edu> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/mfork@fiu.edu> <http://xmlns.com/foaf/0.1/name> "Fork, M"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/mfork@fiu.edu> <http://xmlns.com/foaf/0.1/mbox> "mfork@fiu.edu"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/a-omura@aori.u-tokyo.ac.jp> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/OS/sessions/OS21G/abstracts/OS21G-04/authors/5> .
			<http://abstracts.agu.org/people/a-omura@aori.u-tokyo.ac.jp> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/a-omura@aori.u-tokyo.ac.jp> <http://xmlns.com/foaf/0.1/name> "Omura, A"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/a-omura@aori.u-tokyo.ac.jp> <http://xmlns.com/foaf/0.1/mbox> "a-omura@aori.u-tokyo.ac.jp"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/phpp@alinga.newcasstle.edu.au> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2008/WP/sections/SP/sessions/SP33A/abstracts/SP33A-01/authors/1> .
			<http://abstracts.agu.org/people/phpp@alinga.newcasstle.edu.au> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/phpp@alinga.newcasstle.edu.au> <http://xmlns.com/foaf/0.1/name> "Ponomarenko, P"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/phpp@alinga.newcasstle.edu.au> <http://xmlns.com/foaf/0.1/mbox> "phpp@alinga.newcasstle.edu.au"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/FM11_ED43D_convener_A_Dove> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/ED/sessions/ED43D/conveners/2> .
			<http://abstracts.agu.org/people/FM11_ED43D_convener_A_Dove> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/FM11_ED43D_convener_A_Dove> <http://xmlns.com/foaf/0.1/name> "A Dove"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/SM/sections/ED/sessions/ED42A/abstracts/ED42A-02/authors/1> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/SM/sections/ED/sessions/ED42A/abstracts/ED42A-03/authors/1> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/SM/sections/ED/sessions/ED42A/abstracts/ED42A-04/authors/1> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/SM/sections/ED/sessions/ED42A/abstracts/ED42A-05/authors/1> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2002/SM/sections/ED/sessions/ED42A/abstracts/ED42A-06/authors/1> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://xmlns.com/foaf/0.1/name> "Armwood, A J"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/chellis@grahec.org> <http://xmlns.com/foaf/0.1/mbox> "chellis@grahec.org"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/OS/sections/PO/sessions/PO25H/abstracts/PO25H-15/authors/4> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/C/sessions/C23C/abstracts/C23C-0647/authors/4> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/C/sessions/C53A/abstracts/C53A-02/authors/1> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://xmlns.com/foaf/0.1/name> "Ardhuin, F"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/fanny.ardhuin@ifremer.fr> <http://xmlns.com/foaf/0.1/mbox> "Fanny.Ardhuin@ifremer.fr"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/carole.mariani@upmc.fr> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/T/sessions/T31B/abstracts/T31B-2158/authors/10> .
			<http://abstracts.agu.org/people/carole.mariani@upmc.fr> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/carole.mariani@upmc.fr> <http://xmlns.com/foaf/0.1/name> "Petit, C"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/carole.mariani@upmc.fr> <http://xmlns.com/foaf/0.1/mbox> "carole.mariani@upmc.fr"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/DI/sessions/DI31A/abstracts/DI31A-2152/authors/1> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2011/FM/sections/DI/sessions/DI31A/abstracts/DI31A-2153/authors/2> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2012/FM/sections/T/sessions/T24B/abstracts/T24B-03/authors/1> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/DI/sessions/DI13A/abstracts/DI13A-1853/authors/1> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://xmlns.com/foaf/0.1/name> "Wirth, E A"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/erin.wirth@yale.edu> <http://xmlns.com/foaf/0.1/mbox> "erin.wirth@yale.edu"^^<http://www.w3.org/2001/XMLSchema#string> .
			<http://abstracts.agu.org/people/bjornbrooks@gmail.com> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/B/sessions/B31E/abstracts/B31E-0351/authors/3> .
			<http://abstracts.agu.org/people/bjornbrooks@gmail.com> <http://tw.rpi.edu/schema/hasRole> <http://abstracts.agu.org/meetings/2010/FM/sections/B/sessions/B31E/abstracts/B31E-0352/authors/1> .
			<http://abstracts.agu.org/people/bjornbrooks@gmail.com> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> 

			*/
	}
	
}