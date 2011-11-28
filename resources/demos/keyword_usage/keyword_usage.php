<?php

echo "<html>";
echo "  <body>";

if ( isset($_POST['meeting']) ) { $meeting = $_POST['meeting']; } else { $meeting = ''; }
     

echo "     <center><h3>ESSI Keyword Usage</h3></center>";

echo "     <form name=\"myform\" action=\"keyword_usage.php\" method=\"POST\">";
echo "     <div align=\"center\">";
echo "       <select name=\"meeting\">";
echo "         <option value=\"2005_fm\">2005 Fall Meeting</option>";
echo "         <option value=\"2006_sm\">2006 Spring Meeting</option>";
echo "         <option value=\"2006_fm\">2006 Fall Meeting</option>";
echo "         <option value=\"2007_sm\">2007 Spring Meeting</option>";
echo "         <option value=\"2007_fm\">2007 Fall Meeting</option>";
echo "         <option value=\"2008_sm\">2008 Spring Meeting</option>";
echo "         <option value=\"2008_fm\">2008 Fall Meeting</option>";
echo "         <option value=\"2009_sm\">2009 Spring Meeting</option>";
echo "         <option value=\"2009_fm\">2009 Fall Meeting</option>";
echo "         <option value=\"2010_sm\">2010 Spring Meeting</option>";
echo "         <option value=\"2010_fm\">2010 Fall Meeting</option>";
echo "       </select>";
echo "       <input type=\"submit\" value=\"Submit\">";
echo "     </div>";
echo "     </form>";

$img1 = "images/essi_keyword_cloud_" . $meeting . ".jpg";
$img2 = "images/essi_top_10_" . $meeting . ".png";

if ( $meeting != '' ) {

  if ( $meeting == "2005_fm" ) { $mtg = "2005 Fall Meeting"; }
  if ( $meeting == "2006_sm" ) { $mtg = "2006 Spring Meeting"; }
  if ( $meeting == "2006_fm" ) { $mtg = "2006 Fall Meeting"; }
  if ( $meeting == "2007_sm" ) { $mtg = "2007 Spring Meeting"; }
  if ( $meeting == "2007_fm" ) { $mtg = "2007 Fall Meeting"; }
  if ( $meeting == "2008_sm" ) { $mtg = "2008 Spring Meeting"; }
  if ( $meeting == "2008_fm" ) { $mtg = "2008 Fall Meeting"; }
  if ( $meeting == "2009_sm" ) { $mtg = "2009 Spring Meeting"; }
  if ( $meeting == "2009_fm" ) { $mtg = "2009 Fall Meeting"; }
  if ( $meeting == "2010_sm" ) { $mtg = "2010 Spring Meeting"; }
  if ( $meeting == "2010_fm" ) { $mtg = "2010 Fall Meeting"; }
  
  echo "       <center><h2>$mtg</h2></center><br>";
  echo "       <center><u><h3>ESSI Keyword Tag Cloud</h3></u>";
  echo "       <img height=400 width=600 src=\"$img1\"></center>";

  echo "       <center>";
  echo "<h3><u>Top 10 ESSI Keywords and the Number of Abstracts Containing Them</u></h3>";
  echo "       <img height=400 width=600 src=\"$img2\"></center>";

}

?>

  </body>
</html>