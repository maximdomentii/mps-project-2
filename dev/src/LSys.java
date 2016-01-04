/*==============================================================================
Application:  LSystems in Java
Author:       Paula A. Cooper
Purpose:      University of Calgary CPSC 553 course project

Source:  Please note that much of this code has been converted from a C program
	 into Java.  The initial version of this code (Plant and Fractal Generator, or
	 pfg) was written by Przemyslaw Prusinkiewicz (Copyright (C) 1988) and is
	 found in the "Lindenmayer Systems, Fractals and Plants" SIGGRAPH '88 course
	 notes written by Prusinkiewicz and Hanan.

==============================================================================*/

import java.awt.*;
import java.lang.*;
import java.util.*;

/*===============================================================================
  Applet  LSys
===============================================================================*/

public class LSys extends java.applet.Applet {
    public LSysPanel dispPanel;
    public ParamPanel specPanel;
    public Graphics drawarea;

    public void init() {
        setLayout(new BorderLayout());
        specPanel = new ParamPanel();
        add("North",specPanel);
        dispPanel = new LSysPanel(specPanel);
        add("Center",dispPanel);
        drawarea = dispPanel.getGraphics();

        dispPanel.show();
        specPanel.show();

    }


    public void start() {
        dispPanel.enable();

    }

    public static void main() {

    }
}

/*==============================================================================
class TreeStructure

Author: Maxim Domentii

Purpose: Tree structure containing parameters for building a tree
================================================================================*/
class TreeStructure {
    public String axiom,  ignore ;
    public int depth, scale, angle, clr;
    public java.util.List rules;

    public TreeStructure(String axiom, int depth, int scale, int angle, String ignore, int clr, java.util.List rules){
        this.angle = angle;
        this.axiom = axiom;
        this.clr = clr;
        this.depth = depth;
        this.ignore = ignore;
        this.rules = rules;
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "{" + axiom + ", " + ignore + ", " + depth + ", " + scale + ", " + angle + ", " + clr + ", " + rules + "}";
    }
}

/*==============================================================================
class TreeList

Author: Maxim Domentii

Purpose: List of trees used to draw the image
================================================================================*/
class TreeList {
    public java.util.List<TreeStructure> treeList = new java.util.ArrayList();

    @Override
    public String toString() {
        String str = "";
        for (int i=0; i<treeList.size(); i++){
            str += treeList.get(i) + " ";
        }
        return str;
    }

    public TreeList(){
        java.util.List<String> rules = new ArrayList();
        Random rand = new Random();

        /* Tree A pg. 25 */
        rules.add("* <F> * --> F[+F]F[-F]F");
        treeList.add(new TreeStructure(
                "F", rand.nextInt(6-3)+3, 100, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree B pg. 25 */
        rules = new ArrayList();
        rules.add("* <X> * --> F-[[X]+X]+F[+FX]-X");
        rules.add(" * <F> * --> FF");
        treeList.add(new TreeStructure(
                "X", rand.nextInt(6-3)+3, rand.nextInt(100-30)+30, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree C pg. 25 */
        rules = new ArrayList();
        rules.add(" * <Y> * --> YFX[+Y][-Y]");
        rules.add(" * <X> * --> X[-FFF][+FFF]FX");
        treeList.add(new TreeStructure(
                "Y", rand.nextInt(7-3)+3, rand.nextInt(100-30)+30, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree D pg. 25 */
        rules = new ArrayList();
        rules.add("* <F> * --> FF+[+F-F-F]-[-F+F+F]");
        treeList.add(new TreeStructure(
                "F", rand.nextInt(5-3)+3, rand.nextInt(80-30)+30, rand.nextInt(30-15)+15, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree E pg. 25 */
        rules = new ArrayList();
        rules.add("* <X> * --> F[+X]F[-X]+X");
        rules.add("* <F> * --> FF");
        treeList.add(new TreeStructure(
                "X", rand.nextInt(8-5)+5, 100, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree B pg. 43 */
        rules = new ArrayList();
        rules.add("0 <0> 0 --> 1");
        rules.add("0 <0> 1 --> 1[-F1F1]");
        rules.add("0 <1> 0 --> 1");
        rules.add("0 <1> 1 --> 1");
        rules.add("1 <0> 0 --> 0");
        rules.add("1 <0> 1 --> 1F1");
        rules.add("1 <1> 0 --> 1");
        rules.add("1 <1> 1 --> 0");
        rules.add("* <-> * --> +");
        rules.add("* <+> * --> -");
        treeList.add(new TreeStructure(
                "F1F1F1", rand.nextInt(30-20)+20, rand.nextInt(100-30)+30, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));

        /* Tree C pg. 43 */
        rules = new ArrayList();
        rules.add("0 <0> 0  --> 0");
        rules.add("0 <0> 1 --> 1");
        rules.add("0 <1> 0 --> 0");
        rules.add("0 <1> 1 --> 1[+F1F1]");
        rules.add("1 <0> 0 --> 0");
        rules.add("1 <0> 1 --> 1F1");
        rules.add("1 <1> 0 --> 0");
        rules.add("1 <1> 1--> 0");
        rules.add("* <-> * --> +");
        rules.add("* <+> * --> -");
        treeList.add(new TreeStructure(
                "F1F1F1", rand.nextInt(27-20)+20, rand.nextInt(100-30)+30, rand.nextInt(30-10)+10, "F+-", rand.nextInt(30-23)+23, rules));
    }
}

/*==============================================================================
class ParamPanel

Purpose:   This is the panel containing all of the parameters, and allows
	   them to be adjusted through control widgets contained within it
================================================================================*/
class ParamPanel extends Panel {

    public Button b_apply;

    public boolean b_doApply; 	/* flag whether Apply button has been pressed */
    public boolean b_gotImagePnl=false;	/* flag whether have been given
					   associated drawing panel */
    public LSysPanel pnl_image;

    public Color aColors[];     /* array of colors */

    public Globals glb;         /* globals */

    public java.util.List<TreeStructure> trees = new ArrayList();
    public int treesSize = 100;


    public void populateTrees(){
        trees.clear();
        TreeList treeList = new TreeList();
        Random rand = new Random();
        for (int i=0; i<treesSize; i++) {
            trees.add(treeList.treeList.get(rand.nextInt(treeList.treeList.size())));
        }
    }

    /* constructor */
    public ParamPanel() {
        populateTrees();

		/*----------------------------------------------
		  Constructor:  ParamPanel


		  Purpose:      Sets up the control panel containing the
			        user specified parameters for performing
				the LSystems image

		  ---------------------------------------------*/

        initColors();

        b_doApply = false;
        b_gotImagePnl = false;


        Panel lPanel = new Panel();
        Panel rPanel = new Panel();
        Panel mPanel = new Panel();

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);

        c.fill = GridBagConstraints.HORIZONTAL;	/* fill horizontally*/
        c.weightx=1;  			        /* each of the 3 panels
							   get same horiz space */
        c.insets = new Insets(0,0,0,10); 	/* pad left and right */

        c.gridwidth=1;
        c.anchor = GridBagConstraints.NORTH;

        gridbag.setConstraints(lPanel,c);
        gridbag.setConstraints(rPanel,c);

        gridbag.setConstraints(mPanel,c);

        add(lPanel);			/* add panels left to right */
        add(mPanel);
        add(rPanel);


        GridBagConstraints c_btn = new GridBagConstraints();
        c_btn.gridwidth = 1;
        c_btn.fill = GridBagConstraints.HORIZONTAL;
        c_btn.weightx = 1;
        c_btn.insets = new Insets (30,0,0,0);


		/* Apply button */
        b_apply = new Button("Apply");
        gridbag.setConstraints(b_apply, c_btn);
        lPanel.add(b_apply);


        lPanel.show();
        rPanel.show();
        mPanel.show();

    }

    public void setImagePanel(LSysPanel pnl_lsys){
        pnl_image = pnl_lsys;
        b_gotImagePnl = true;
    }


    /*-----------------------------------------------------------
      Method:  handleEvent
      Purpose: handle button presses and other user interaction
             performed in the control panel
      ---------------------------------------------------------*/
    public boolean handleEvent(Event e) {

        if (e.target instanceof Button) {
            if (e.target==b_apply) {
                b_doApply=true;
                populateTrees();
				/* apply the most recent control panel settings
				   to redraw the image in the related image
				   panel */
                if (b_gotImagePnl==true) {
                    pnl_image.repaint();
                }

            }
            return(true);
        }


        return(false);  /* return false for unprocessed events */
    }

    public void paint(Graphics g) {

    }


    /*=====================================================================
    Method:   initColors
    Purpose:  Initialize Array of Colors

    =====================================================================*/
    public void initColors() {

        int i;
        float j;

        int start;
        int numShades=5;
        float shadeInc = 1/(float)numShades;

        aColors = new Color[glb.MAXCOLORS];  /* set array size */

		/* White to Black */
        start = 0;
        for (i=start, j=1;i<start+6;j-=shadeInc,i++) {
            aColors[i] = new Color(j,j,j);
        }

        start = 6;
		/* Red to almost Magenta */
        for (i=start,j=0;i<start+5;j+=shadeInc,i++){
            aColors[i] = new Color((float)1,(float)0,j);
        }


		/* Magenta to almost Blue */
        start +=5;
        for (i=start,j=1;i<start+5;j-=shadeInc,i++){
            aColors[i] = new Color(j,(float)0,(float)1);
        }


		/* Blue to almost Cyan */
        start +=5;
        for (i=start,j=0;i<start+5;j+=shadeInc,i++){
            aColors[i] = new Color((float)0,j,(float)1);
        }

		/* Cyan to almost Green */
        start +=5;
        for (i=start,j=1;i<start+5;j-=shadeInc,i++){
            aColors[i] = new Color((float)0,(float)1,j);
        }



		/* Green to almost Yellow */
        start +=5;
        for (i=start,j=0;i<start+5;j+=shadeInc,i++){
            aColors[i] = new Color(j,(float)1,(float)0);
        }

		/* Yellow to almost Red */
        start +=5;
        for (i=start,j=1;i<start+5;j-=shadeInc,i++){
            aColors[i] = new Color((float)1,j,(float)0);
        }

    }

}

/*==============================================================================
class      ClrPanel
Content:   Panel with Color Bar (for selecting start color)

===============================================================================*/
class ClrPanel extends Panel {
    public Globals glb;         /* globals */
    Color aColors[];

    /* constructor */
    public ClrPanel(Color aClrArray[]) {

        aColors = aClrArray;
    }

    public void paint(Graphics g) {
        boolean b;
        Dimension pnldim;
        int i;
        int clrBarWd;
        int startx;

        pnldim = this.size();

        clrBarWd = (pnldim.width - (25+80))/glb.MAXCOLORS;


        for (startx=25,i=0;i<glb.MAXCOLORS;i++,startx+=(clrBarWd+1)) {
            g.setColor(aColors[i]);
            g.drawLine(startx,0,startx+clrBarWd,0);
            g.drawLine(startx,1,startx+clrBarWd,1);
        }





    }


    public Insets insets() {
		/* create border around outside of panel */
        return new Insets(1,1,1,1);
    }
}

/*==============================================================================
class      LSysPanel
Content:   main drawing area (area where LSystems image appears)

===============================================================================*/
class LSysPanel extends Panel {

    public Generate2 pixgen;
    public ParamPanel pnl_Specs;

    /* constructor */
    public LSysPanel(ParamPanel specpanel) {
        pnl_Specs = specpanel;
        setLayout(new BorderLayout());
        pixgen = new Generate2();

        pnl_Specs.setImagePanel(this); /* tell spec panel to draw pictures
						  in this panel */


    }

    public void paint(Graphics g) {
        boolean b;
        Dimension pnldim;
        double height, width;

        pnldim = this.size();


		/* draw a border */
        g.setColor(Color.black);
        g.drawRect(0,0,pnldim.width-1,pnldim.height-1);


		/* only draw if the Apply button has been pressed at least once */
        if (pnl_Specs.b_doApply==true) {

            height = (double)pnldim.height;
            width = (double)pnldim.width;

			/* run the program */
            for (int i=0; i<pnl_Specs.treesSize; i++) {
                pixgen.ProcessLSys(i, pnl_Specs, g, height, width);
            }

        }


    }


    public Insets insets() {
		/* create border around outside of panel */
        return new Insets(1,1,1,1);
    }
}


/*===============================================================================
===============================================================================*/
class Generate2 extends Generate {
    public Turtle curpos, prevpos;		/* current position, previous position */
    public Pixel startpos;			/* start position */
    public Box boxdim;
    public Production aRules[];		/* array of production rules */
    public int iNumRules;			/* length of aRules array */
    public Parameter Specs;			/* specifications on what/how to derive */
    public String sCurLvl;			/* string with currently evaluated LSystems pattern */
    public StringBuffer sNextLvl;		/* next level of LSystems pattern
						   ... used when evaluating
						       pattern */
    public int iCur;			/* position in sCurLvl */
    public String Ignore;			/* characters to be ignored */
    public int nIncSz;			/* size of a step when go forward */

    public Globals glb;                     /* globals */

    /*-----------------
      items for drawing lines of
      different width
      -----------------*/
    public int left_adj=0;			/* left width adjustment */
    public int right_adj=0;              	/* right width adjustment */
    int iLnWidth=1;                           /* current line width */
    int iHalf_Wd=0;            		/* half line width, floored */
    double RotRad=0;                          /* radians rotated in a
						   single rotation */



    /* constructor */
    public Generate2() {
		/* instantiate object variables */
        this.curpos = new Turtle();
        this.prevpos = new Turtle();
        this.startpos = new Pixel();
        this.boxdim = new Box();


        this.aRules = new Production[0];
        this.iNumRules = 0;
        this.Specs = new Parameter();
        this.sCurLvl = "";
        this.sNextLvl = new StringBuffer();
        this.iCur = 0;
        this.Ignore = "Ff+-|;:#!";
        this.nIncSz = 0;

    }

    public static void DoGen(){
    }

    /*--------------------------------------------------------------------
    Method:   ProcessLSys
    Purpose:  generate the LSystems string from the current axiom and
          interpret the string to create the image

    Returns:  true  -- success
          false -- otherwise
    ---------------------------------------------------------------------*/
    public boolean ProcessLSys(int treeIndex, ParamPanel pnl_specs, Graphics g,double pnl_ht, double pnl_wd){

        Random rand = new Random();

        startpos.horiz = (int) (rand.nextInt(50 + 50)) - 50;
        startpos.vert = (int) (rand.nextInt(30 + 30)) - 30;


        GetSpecs(pnl_specs, treeIndex);

        Derive();

        Interpret(g, pnl_ht, pnl_wd, pnl_specs.aColors);

        return(true);
    }

	/*------------------------------------------------------------------
	Method:   GetSpecs
	Purpose:  get the specifications such as the rules and the starting axiom
		  out of the GUI widgets (listbox, etc) and place them in their
		  designated data structures

	Parameters:  pnl_specs - panel containing widgets with specifications
		 		 of how to draw LSystems item

	Returns:  true  -- if got everything and rules had proper syntax
		  false -- otherwise
	-------------------------------------------------------------------*/

    public boolean GetSpecs(ParamPanel pnl_specs, int index){
        int iPred, iPost, iSucc;	/* separator indexes in rule */
        String sRule;			/* holds rule being parsed */

        iNumRules = pnl_specs.trees.get(index).rules.size();
        this.aRules = new Production[iNumRules];

        Specs.nLnType = glb.LN_STRT;

        Specs.axiom = pnl_specs.trees.get(index).axiom;
        Ignore = pnl_specs.trees.get(index).ignore;
        Specs.angle = pnl_specs.trees.get(index).angle;
        Specs.scale = pnl_specs.trees.get(index).scale;
        Specs.depth = pnl_specs.trees.get(index).depth;
        Specs.start_clr = pnl_specs.trees.get(index).clr;

        for (int i = 0; i < iNumRules; i++) {
            sRule = (String) pnl_specs.trees.get(index).rules.get(i);


			/* parse through the rule for the separators
				'<','>','-->'
			   where the rule is of the form:

				leftcontext < predecessor > rightcontext --> successor
			*/
            iPred = sRule.indexOf('<');
            if (iPred != -1) {
                iPost = sRule.indexOf('>',iPred);
            } else {

                System.out.println("Error!  Missing '<' in rule.");
                return(false);
            }
            if (iPost != -1) {
                iSucc = sRule.indexOf("-->",iPost);
            } else {
                System.out.println("Error!  Missing '>' in rule.");
                return(false);
            }
            if (iSucc == -1) {
                System.out.println("Error!  Missing '-->' in rule.");
                return(false);
            }


			/* put the rule into the array of rules */
            aRules[i] = new Production();

			/* left context */
            aRules[i].lCtxt = sRule.substring(0,iPred);
            aRules[i].lCtxt = FixStr(aRules[i].lCtxt);
            aRules[i].lCtxtLen = aRules[i].lCtxt.length();

			/* predecessor */
            aRules[i].pred = sRule.substring(iPred+1,iPost);
            aRules[i].pred = FixStr(aRules[i].pred);
            aRules[i].predLen = aRules[i].pred.length();

			/* right context */
            aRules[i].rCtxt = sRule.substring(iPost+1, iSucc);
            aRules[i].rCtxt = FixStr(aRules[i].rCtxt);
            aRules[i].rCtxtLen = aRules[i].rCtxt.length();

			/* successor */
            aRules[i].succ = sRule.substring(iSucc+3);
            aRules[i].succ = FixStr(aRules[i].succ);
            aRules[i].succLen = aRules[i].succ.length();


        }

        return(true);

    }

    /*------------------------------------------------------------------
    Method:   FixStr
    Purpose:  adjust the string passed (assumed to be a substring portion
          of a rule such as the:
                 left context,
                 right context,
                 predecessor, or
                 successor)

          so that if it is simply the '*' character (which stands for
          the empty string), it is replaced by an empty string

    Returns:
          the new String contents
    -------------------------------------------------------------------*/
    public String FixStr(String sStr) {
        sStr = sStr.trim();
        if (sStr.equals("*")==true) {
            sStr = new String("");
        }

        return(sStr);

    }

    /*--------------------------------------------------------------------
    Method:   Derive
    Purpose:  determine the LSystems pattern created using the current
          specifications and production rules

    Algorithm:

    Returned:
        true -- success in derivation
        false -- otherwise
    Globals Changed:

        sCurLvl - will contain final pattern created

    Globals Used:
        sCurLvl - contains

    --------------------------------------------------------------------*/
    public boolean Derive(){
        int i;

        sCurLvl = Specs.axiom;
        iCur = 0;
        sNextLvl = new StringBuffer();

		/* derive each level */
        for(i=1; i<=Specs.depth;i++){

            iCur=0;
			/* keep going until done deriving current level string */
            while(iCur < sCurLvl.length()){
                ApplyProd(FindProd());
            }
            sCurLvl = new String(sNextLvl);
            sNextLvl = new StringBuffer();

        }

        return(true);

    }

    /*-------------------------------------------------------------------
    Method:   ApplyProd
    Purpose:  replace a predecessor with its successor based on a
          specific production rule

    Algorithm:	If we found a production rule to match we copy the
            successor for the current predecessor into the next
            level (thus applying the production rule).  If no
            production rule matched this predecessor then we
            copy the first character of this predecessor string
            down into the next level and shift the start of
            the predecessor to the right one (no longer consider
            this character part of the predecessor)


    Params:   RuleIdx  -  index for production rule (-1 if no production
                   rule should be applied)
    Globals Changed:
          iCur - set to point to next predecessor to be evaluated
             (skips past the current one which just had the
                  production rule applied)
          sNextLvl - has the successor for the production rule
                 just applied appended to the end of it
    --------------------------------------------------------------------*/
    public void ApplyProd(int RuleIdx){
        if (RuleIdx != -1){
			/* apply production */
            sNextLvl.append(aRules[RuleIdx].succ);	/* apply rule */
            iCur += aRules[RuleIdx].predLen;	/* skip past current predecessor to next predecessor */
        } else {
			/* move one character down to next level (out of
			   predecessor) */
            sNextLvl.append(sCurLvl.charAt(iCur));
            iCur++;   /* skip past one character */
        }

    }

    /*------------------------------------------------------------------
    Method:    FindProd
    Purpose:   find the production rule that applies to the predecessor
           at the current position (iCur) in the current level
           (sCurLvl)
           Tests for context free, context sensitive left, context
           sensitive right, and context sensitive right and left.

           Notice that the FIRST rule that matches (whether context
           free or context sensitive) is applied!

    Returns:   index of rule if found
              otherwise... -1  (no rule found)
    -------------------------------------------------------------------*/
    public int FindProd(){

        int i;
        i=0;

        while(i < iNumRules){


			/* if the predecessor, left condition, and right
			   condition match then the production matches so
			   return it, otherwise try the next production rule */

            if (prefix(aRules[i].pred,sCurLvl,iCur)&&
                    rcondiff(aRules[i].rCtxt,sCurLvl,iCur+aRules[i].predLen)&&
                    lcondiff(aRules[i].lCtxt,sCurLvl,iCur-1)){
                return(i);	/* a match! */
            } else {
                i++;
            }

        }

        return(-1);	/* no rule found */


    }

    /*------------------------------------------------------------------
    Method:	  doIgnore
    Purpose:  determine whether the current character is one of the
          "irrelevant" or "ignorable" characters in terms of
          processing an LSystems pattern

    Parms:	  sStr - string to check in
          Idx  - index into string

    Returns:  true -- yes, should ignore the current char
          false -- no, should not ignore the current char
    -------------------------------------------------------------------*/
    public boolean doIgnore (String sStr, int Idx){

        int i;

        for (i=0;i < Ignore.length();i++) {
            if (sStr.charAt(Idx)==Ignore.charAt(i)) {
                return(true);
            }
        }

        return(false);

    }

    /*------------------------------------------------------------------
    Method:   prefix
    Purpose:  see if the rule string is the starting portion found at the
              current position (cIdx) in the derivation string

    Return:   true - yes, is a prefix
              false - no, is not a prefix

    -------------------------------------------------------------------*/

    public boolean prefix(String sRule, String sCur, int cIdx){
        return(sCur.regionMatches(cIdx,sRule,0,sRule.length()));

    }

    /*-------------------------------------------------------------------
    Method:  rcondiff
    Purpose:   check for a matching right context in a rule and the
               current position in the "derivation" string

    Parameters:     sRule -- right context in a rule
                    sCur  -- derivation string
                    cIdx  -- current position in derivation string

    Returns:      true - yes, the rule and right context matched
                  false - no match

    --------------------------------------------------------------------*/
    public boolean rcondiff(String sRule, String sCur, int cIdx){

        int rIdx=0;

        while(true) {

            if (rIdx >= sRule.length()) {
                return(true);       /* success! */
            } else if (cIdx >= sCur.length()) {
				/* we were at farthest right side of the current
				   derivation level's string (sCur).. so there is
				   no right context.. meaning it would only have
				   matched if there no right context in rule */
                return(false);	    /* no match!! */
            } else if (doIgnore(sCur,cIdx)==true) {
                cIdx++;
            } else if (sCur.charAt(cIdx) == '[') {
                cIdx = this.skipright(cIdx,sCur);
                if (cIdx == -1) {
                    System.out.println("Error!  Missing ']' to terminate branch");
                    return(false);
                }
            } else if (!(sRule.charAt(rIdx) == sCur.charAt(cIdx))) {
                return(false);      /* no match!! */
            } else {
				/* matched on this character, so try next */
                rIdx++;
                cIdx++;
            }

        }

    }

    /*------------------------------------------------------------------
    Method:  skipright
    Purpose: skip over a branching section in a string (ie. from [ to
             ]) and also any subbranches contained in that branch

    Parameters:
                    sIdx  - index into string (assumed to be on first '['
                    sStr  - string to search in

    Returns:        -1 -- error!!! didn't find closing ]
                    other -- index into string after matching ]

    -------------------------------------------------------------------*/
    int skipright(int sIdx, String sStr) {
        int level = 0;
        sIdx++;         /* get past first [ */
        while (sIdx < sStr.length()) {
            switch(sStr.charAt(sIdx)) {
                case '[':
                    level++;
                    break;

                case ']':
                    if (level==0) {

                        return(++sIdx);
                    } else {

                        level--;
                    }
                    break;
                default:
                    break;

            }


            sIdx++;
        }

        return((int)-1);     /* no matching ] */
    }

    /*-------------------------------------------------------------------
    Method:  lcondiff
    Purpose:   check for a matching left context in a rule and the
               current position in the "derivation" string.
         Notice that we move through the string from left to right

    Parameters:     sRule -- right context in a rule
                    sCur  -- derivation string
                    cIdx  -- current position in derivation string

    Returns:      true - yes, the rule and left context matched
                  false - no match

    --------------------------------------------------------------------*/
    public boolean lcondiff(String sRule, String sCur, int cIdx){

        int rIdx=sRule.length();   /* start at end of rule */
        rIdx--;


        while(true) {

            if (rIdx < 0) {
                return(true);       /* success! */
            } else if(cIdx < 0) {
				/* we were at farthest left side of the current
				   derivation level's string (sCur).. so there is
				   no left context.. meaning it would only have
				   matched if there was no left context in rule */
                return(false);	    /* no match!! */
            } else if (doIgnore(sCur,cIdx)==true){
                cIdx--;
            } else if(sCur.charAt(cIdx) == '[') {
                cIdx--;
            } else if (sCur.charAt(cIdx) == ']') {
                cIdx = this.skipleft(cIdx,sCur);
                if (cIdx == -1) {
                    System.out.println("Error!  Missing ']' to terminate branch");
                    return(false);
                }
            } else if (!(sRule.charAt(rIdx) == sCur.charAt(cIdx))) {
                return(false);      /* no match!! */
            } else {
				/* matched on this character, so try next one */

                rIdx--;
                cIdx--;

            }


        }

    }

    /*------------------------------------------------------------------
    Method:  skipleft
    Purpose: skip over a branching section in a string (ie. from [ to
             ]) and also any subbranches contained in that branch.  Notice
         that we go through through the string from right to left

    Parameters:
                    sIdx  - index into string (assumed to be on first '['
                    sStr  - string to search in

    Returns:        -1 -- error!!! didn't find closing ]
                    other -- index into string after matching ]

    -------------------------------------------------------------------*/
    int skipleft(int sIdx, String sStr) {
        int level = 0;
        sIdx--;         /* get past first ] */
        while (sIdx > 0) {
            switch(sStr.charAt(sIdx)) {
                case ']':
                    level++;
                    break;

                case '[':
                    if (level==0) {

                        return(--sIdx);
                    } else {

                        level--;
                    }
                    break;
                default:
                    break;

            }


            sIdx--;
        }

        return((int)-1);     /* no matching ] */
    }

	/*----------------------------------------------------------------------
	Method:    Interpret
	Purpose:   interpret the meaning of the LSystems string

	Parms:	   g - graphics object to do drawing in
		   pnl_ht - height of drawing area
		   pnl_wd - width of drawing area

	----------------------------------------------------------------------*/

    public boolean Interpret(Graphics g, double pnl_ht, double pnl_wd, Color aColors[]) {
        nIncSz = 1;
		/* determine points, but don't actually draw.. just find
		   dimensions of bounding box */
        draw (sCurLvl,nIncSz,Specs.angle,false,g, aColors);

        SetDrawParam(pnl_ht, pnl_wd);

		/* draw it!! */

        draw (sCurLvl,nIncSz,Specs.angle,true,g, aColors);

        return(true);
    }

	/*----------------------------------------------------------------------
	Method:   SetDrawParam
	Purpose:  Adjusts the step size (nIncSz) as well as the starting position
		  of the turtle based upon the bounding box that was required
		  for a step size of one
	Assumptions:	assumes that the bounding box contains the bounding
			dimensions given a step size of one


	Globals Changed:
			nIncSz  -- adjust increment size for drawing in current
				   panel dimensions

			startpos.horiz -- starting position
			startpos.vert
	------------------------------------------------------------------------*/

    public void SetDrawParam (double pnl_ht, double pnl_wd){

        double xscale, yscale, sc;

		/* determine how relate a "step" to the width and height
		   of the actual screen */
        xscale = (pnl_wd/(boxdim.xmax-boxdim.xmin));
        yscale = (pnl_ht/(boxdim.ymax-boxdim.ymin));



		/* determine whether the width or height is the tighter bound */
        if(xscale>yscale){
            sc = yscale;
        } else {
            sc = xscale;
        }


		/* determine what percentage of the full step should be used
		   based on the scale factor.. if the scale is the maximum
		   scale then the full sized step will be taken */
        nIncSz = (int)(Math.floor((double)((sc*Specs.scale)/MAXSCALE)));

        startpos.horiz = (int)((pnl_wd - (nIncSz*(boxdim.xmin+boxdim.xmax-1)))/2);
        startpos.vert = (int)((pnl_ht - (nIncSz*(boxdim.ymin+boxdim.ymax-1)))/2);


    }

    /*----------------------------------------------------------------------
    Method:    drawWideLine

    Purpose:   draw a line of a specified width


           If a line of single width is desired the normal drawline
           facility is called.

           If the line is of width greater than one, we must create
           a polygon (since there is no method (?) of specifying line
           width in Java, and rectangles cannot be used because they
           only permit a horizontal orientation)

    ----------------------------------------------------------------------*/
    void drawWideLine(Turtle startpt, Turtle endpt, Graphics g) {
        int aPolyX[] = new int[4];	/* array of X coords in polygon */
        int aPolyY[] = new int[4];	/* array of Y coords in polygon */

        Turtle ll1 = new Turtle();
        Turtle lr1 = new Turtle();
        Turtle ll2 = new Turtle();
        Turtle lr2 = new Turtle();
        Turtle ll3 = new Turtle();
        Turtle lr3 = new Turtle();
        Turtle ul = new Turtle();
        Turtle ur = new Turtle();
        double dAng;


        if (iLnWidth==1) {

            g.drawLine((int)startpt.x, (int)startpt.y, (int)endpt.x, (int) endpt.y);

        } else {

			/*-------------------
			  assume the start point is at
			  the origin and that the direction is
			  North.  Add a left most point and
			  right most point, to give the spread
			  of the line width across the x axis.

			  Then, rotate these two outer points
			  by the amount that the line is to be
			  rotated.  This gives us the points
			  ll2 and lr2 which have been rotated about
			  the origin.

			  Add these new points (ll2, lr2) to
			  the start point to give the lower left
			  and lower right points (ll3 and lr3)
			  of the polygon to be drawn
			  -------------------*/

            ll1.x = left_adj;
            ll1.y = 0;
            lr1.x = right_adj;
            lr1.y = 0;

            dAng = -(endpt.dir*RotRad);
            ll2.x = (Math.cos(dAng)*ll1.x) -
                    (Math.sin(dAng)*ll1.y);
            ll2.y = (Math.sin(dAng)*ll1.x) +
                    (Math.cos(dAng)*ll1.y);
            ll3.x = ll2.x+startpt.x;
            ll3.y = ll2.y+startpt.y;

            lr2.x = (Math.cos(dAng)*lr1.x) -
                    (Math.sin(dAng)*lr1.y);
            lr2.y = (Math.sin(dAng)*lr1.x) +
                    (Math.cos(dAng)*lr1.y);
            lr3.x = lr2.x + startpt.x;
            lr3.y = lr2.y + startpt.y;



			/*-------------------------
			  Add the points that were rotated
			  about the origin to the coordinates
			  for the endpoint of the line, giving
			  us the upper left and upper right
			  points of the polygon (ul and ur)
			  --------------------------*/

            ul.x = ll2.x+endpt.x;
            ul.y = ll2.y+endpt.y;

            ur.x = lr2.x+endpt.x;
            ur.y = lr2.y+endpt.y;


			/*------------
			  Store arrays of X and Y
			  coordinates for drawing
			  Polygons
			  ------------*/
            aPolyX[0] = (int)ll3.x;
            aPolyX[1] = (int)ul.x;
            aPolyX[2] = (int)ur.x;
            aPolyX[3] = (int)lr3.x;

            aPolyY[0] = (int)ll3.y;
            aPolyY[1] = (int)ul.y;
            aPolyY[2] = (int)ur.y;
            aPolyY[3] = (int)lr3.y;

            g.fillPolygon(aPolyX,aPolyY,4);
        }

    }

    /*-----------------------------------------------------------------------
    Method:    draw



        1) Start by drawing to the North

        - keep a direction specification for the turtle where
              dir = 0 is North
            dir = 1 is 360/iAngFac degrees clockwise of north
                   (ie. one rotation right)
            dir = 2 is 2*(360/iAngFac) degrees clockwise of north
                   (ie. two rotations right)
            etc...

            so rotations right add to the dir value,
            rotations left subtract


    -----------------------------------------------------------------------*/
    boolean draw(String sPattern, int iStepSz, int iAngFac, boolean bDoDraw, Graphics g, Color aColors[]) {

        double SI[] = new double[MAXANGLE];
        double CO[] = new double[MAXANGLE];
        Turtle PosStack[] = new Turtle[MAXSTACK];
        CurveTurtle CrvPosStack[] = new CurveTurtle[MAXSTACK];
        double dAng = -TWO_PI/4;       /* -90=270 Degrees */
        int i;
        int iMaxDir;		/* maximum direction counter value */
        int iStkIdx=-1;		/* index into stack array */
        int iMaxClrIdx=glb.MAXCOLORS-1;
        CurveTurtle ct = new CurveTurtle();
        int iHalfAngFac = iAngFac/2;         /* half of angle factor
						        (for turning around) */



		/*----
		  Structures for storing first 3 points in current curve
		  -----*/
        Turtle aFirstPts[] = new Turtle[3];
        int iPtsIdx=0;


		/*-------
		  Structures for creating and storing 4 vertices on a rectangle
		  for producing lines of different width
		  -----*/


        left_adj=0;			/* left width adjustment */
        right_adj=0;              	/* right width adjustment */
        iHalf_Wd = 0;        		/* half line width, floored */


        RotRad = -(TWO_PI/(double)iAngFac);

        g.setColor(aColors[1]);


        boxdim.xmin=boxdim.xmax=curpos.x = prevpos.x = (double)(startpos.horiz)+0.5;
        boxdim.ymin=boxdim.ymax=curpos.y = prevpos.y = (double)(startpos.vert)+0.5;
        curpos.dir = prevpos.dir = 0;
        curpos.clrIdx = Specs.start_clr;
        curpos.lnWidth = iLnWidth = 1;

        /* precalculate the Sine/Cosine values for all of the possible
		   angle rotations that the turtle can perform */

        for (i = 0; i < iAngFac;i++) {

            SI[i] = (double)(iStepSz) * Math.sin(dAng);
            CO[i] = (double)(iStepSz) * Math.cos(dAng);

            dAng += TWO_PI/(double)iAngFac;

        }

        iMaxDir = --iAngFac;

        g.setColor(aColors[curpos.clrIdx]);
        iLnWidth = curpos.lnWidth;

		/* move through string from left to right and
		   manipulate the turtle on the screen as the
		   pattern string specifies */

        for (i = 0; i < sPattern.length(); i++) {

            switch (sPattern.charAt(i)){

                case '|':	/* turn around (180 degrees) */
				/*---------
				  if current direction is more than a 180
				  degree rotation, then subtract 180
				  or if less than 180 degrees then add
				  ---------*/
                    if (curpos.dir>=iHalfAngFac) {
                        curpos.dir -= iHalfAngFac;
                    } else {
                        curpos.dir += iHalfAngFac;
                    }
                    break;

                case '#':	/* increment line width */
                    curpos.lnWidth++;
                    iHalf_Wd=(int)(Math.floor(curpos.lnWidth/2));

				/*------------
				  if odd width, adjust x same both
				  left and right

				  if even width, adjust x one less
				  on left
				  ---------*/
                    if ((curpos.lnWidth%2)==1) {
                        left_adj  = -iHalf_Wd;
                        right_adj = iHalf_Wd;
                    } else {
                        left_adj  = -(iHalf_Wd-1);
                        right_adj = iHalf_Wd;
                    }
                    iLnWidth = curpos.lnWidth;
                    break;

                case '!':       /* decrement line width */
                    if (curpos.lnWidth>1) {
                        curpos.lnWidth--;

                        iHalf_Wd=(int)(Math.floor(curpos.lnWidth/2));

					/*------------
					  if odd width, adjust x same both
					  left and right

					  if even width, adjust x one less
					  on left
					  ---------*/
                        if ((curpos.lnWidth%2)==1) {
                            left_adj  = -iHalf_Wd;
                            right_adj = iHalf_Wd;
                        } else {
                            left_adj  = -(iHalf_Wd-1);
                            right_adj = iHalf_Wd;
                        }

                    }
                    iLnWidth = curpos.lnWidth;
                    break;

                case ';':       /* increment color */
                    if (curpos.clrIdx == iMaxClrIdx) {
                        curpos.clrIdx = 0;
                    } else {
                        curpos.clrIdx++;
                    }
                    g.setColor(aColors[curpos.clrIdx]);
                    break;

                case ':':	/* decrement color */
                    if (curpos.clrIdx == 0) {
                        curpos.clrIdx = iMaxClrIdx;
                    } else {
                        curpos.clrIdx--;
                    }
                    g.setColor(aColors[curpos.clrIdx]);
                    break;

                case '+': 	/* right */
                    if (curpos.dir < iMaxDir) {
                        curpos.dir++;
                    } else {
                        curpos.dir = 0;   /* did a full 360 */
                    }
                    break;

                case '-':    	/* left */
                    if (curpos.dir > 0) {
                        curpos.dir--;
                    } else {
                        curpos.dir = iMaxDir;
                    }
                    break;

                case '[':	/* branching */

                    iStkIdx++;
                    if (iStkIdx < MAXSTACK) {
					/* push current position onto stack */
                        PosStack[iStkIdx] = new Turtle();
                        PosStack[iStkIdx].getvals(curpos);
                        CrvPosStack[iStkIdx] = new CurveTurtle();
                        CrvPosStack[iStkIdx].getvals(ct);

                    } else {
                        System.out.println("Error!  Too many branches.");
                        return(false);
                    }
                    break;

                case ']':	/* returning from branch */

                    if (iStkIdx < 0) {
                        System.out.println("Error!  Missing closing ']'");
                        return(false);
                    } else {
					/* pop branching position off of stack */
                        curpos.getvals(PosStack[iStkIdx]);
                        prevpos.getvals(curpos);

                        ct.getvals(CrvPosStack[iStkIdx]);
                        iStkIdx--;
                    }
                    iLnWidth = curpos.lnWidth;
                    g.setColor(aColors[curpos.clrIdx]);
                    break;

                case 'F':	/* go forward and draw line */
				/* x' = x + cos(ang)
				   y' = y + sin(ang)
				*/
                    curpos.x += CO[curpos.dir];
                    curpos.y += SI[curpos.dir];
				/* draw line or update box dimension settings */
                    if (bDoDraw==true) {

                        drawWideLine(prevpos, curpos, g);

                        prevpos.getvals(curpos);  /* copy position */

                    } else {
                        UpdateBox(curpos);

                    }
                    break;

                case 'f':	/* go forward without drawing line */
				/* x' = x + cos(ang)
				   y' = y + sin(ang)
				*/
                    curpos.x += CO[curpos.dir];
                    curpos.y += SI[curpos.dir];
				/* update previous position or update box dimension settings */
                    if (bDoDraw==true) {
					/* draw first and last curve segments of previous
					   curve */
                        prevpos.getvals(curpos);  /* copy position*/
                    } else {
                        UpdateBox(curpos);
                    }
                    break;

            }
        }
        return(true);

    }

    /*----------------------------------------------------------------------
    Method:   UpdateBox

    Purpose:  Extend the current dimensions of the box if the position
          sent specifies an area outside of the box
    ----------------------------------------------------------------------*/
    void UpdateBox(Turtle boxpos) {

        boxdim.xmin = Math.min(boxpos.x, boxdim.xmin);
        boxdim.xmax = Math.max(boxpos.x, boxdim.xmax);
        boxdim.ymin = Math.min(boxpos.y, boxdim.ymin);
        boxdim.ymax = Math.max(boxpos.y, boxdim.ymax);
    }

}

/*========================================================================
  Class Globals

  This class contains globals to be shared among all of the objects

  =======================================================================*/
abstract class Globals extends Object {
    static final int MAXCOLORS = 36;      /* max number of colors in array */

    static final int LN_STRT=0;           /* line types */
}
/*=========================================================================
Class Generate

This class is basically the common constants for the generations of
the Lsystems string
===========================================================================*/
abstract class Generate extends Object {

    /* constants */
    static final int MAXPROD = 50;   /* max depth of productions */
    static final int MAXAXIOM = 100; /* max length of axiom */
    static final int MAXSTR = 30000; /* max size of final generation string */
    static final int MAXCHARS = 256; /* max number of ASCII character codes */
    static final int MAXIGNORE = 50; /* max number of ignored symbols */
    static final int MAXANGLE = 40;  /* max number of rotations in 360 degrees */
    static final int MAXSCALE = 100; /* if scale is 100, full screen, 0 none*/
    static final double TWO_PI = 6.2831853;
    static final int MAXSTACK = 40;  /* max number of branches in pattern */
    static final int LEFT = 1;       /* dimensions of canvas */
    static final int RIGHT = 510;
    static final int TOP = 1;
    static final int BOTTOM = 510;



}

/*==============================================================================
Class Parameter

Contents:
        contains the input parameters given by the user
==============================================================================*/
class Parameter extends Object {
    public String axiom;		    /* axiom */
    public int depth;                   /* depth of derivation */
    public int angle;                   /* angle factor (number of divisions in 360 degrees) */
    public int scale;                   /* scaling factor (how many pixels a "step" is) */

    public int nLnType;		    /* type of line to be drawn
							(straight,
							 Hermite curve,
							 B-Spline) */

    public int start_clr;		    /* starting color */

    /* constructor - initialize */
    Parameter(){


    }

}

/*==============================================================================
Class Production

Contents:
        contains the the four portions of a single production rule:
                left context
                predecessor
                right context
                successor

        where a rule is written:

                left context <predecessor> right context --> successor

        and in essence the predecessor translates into the successor, and
        the left and right context provide further information to choose
        the proper production rule based on what appears before and after
        the predecessor

==============================================================================*/
class Production extends Object {
    public String lCtxt;      /* left context string */
    public int lCtxtLen;            /* left context string length */
    public String pred;       /* predecessor string */
    public int predLen;             /* predecessor string length */
    public String rCtxt;      /* right context string */
    public int rCtxtLen;            /* right context string length */
    public String succ;       /* successor string */
    public int succLen;             /* successor string length */

}

/*==============================================================================
Class:    Pixel
Contents:  pixel position in the screen

	  horiz - horizontal position
	  vert - vertical position

==============================================================================*/
class Pixel extends Object {
    public int horiz;	/* horizontal */
    public int vert;	/* vertical */

}

/*==============================================================================
Class:   Box
Contents: defines dimensions of the bounding box for a drawing

================================================================================*/
class Box extends Object {
    double xmin, xmax;
    double ymin, ymax;
}


/*===============================================================================
Class:    Turtle
Contents:  the current orientation of the turtle in the drawing

	x - x coordinate
	y - y coordinate
	dir - direction of turtle (ie. the number of rotations right from the
				       start position)
	clrIdx - current color index of lines
	lnWidth - width of lines
===============================================================================*/
class Turtle extends Object {
    public double x;
    public double y;
    public int dir;
    public int clrIdx;
    public int lnWidth;

    /*---------------------------------------------------------------------
    Method:   getvals
    Purpose:  copy over the values from another turtle structure into
          this one
    ---------------------------------------------------------------------*/
    public void getvals(Turtle t) {
        this.x = t.x;
        this.y = t.y;
        this.dir = t.dir;
        this.clrIdx = t.clrIdx;
        this.lnWidth = t.lnWidth;
    }
}


/*===============================================================================
Class:     CurveTurtle
Contents:  contains turtle information for drawing a hermite curve

===============================================================================*/
class CurveTurtle extends Object {

    public Turtle p1;   /* first tangent vector, or first control point */
    public Turtle p2;   /* start position for curve, or second control pt */
    public Turtle p3;   /* end position for curve, or third control pt */
    public Turtle p4;   /* end tangent vector, or fourth control pt */

    public boolean bDoDraw;      /* flag whether draw between points or not */


    /*------------------
      Constructor
    -------------------*/
    CurveTurtle() {
        this.p1 = new Turtle();
        this.p2 = new Turtle();
        this.p3 = new Turtle();
        this.p4 = new Turtle();

    }

    /*---------------------------------------------------------------------
    Method:   getvals
    Purpose:  copy over the values from another curve turtle into
          this one
    ---------------------------------------------------------------------*/
    public void getvals(CurveTurtle t) {
        this.p1.getvals(t.p1);
        this.p2.getvals(t.p2);
        this.p3.getvals(t.p3);
        this.p4.getvals(t.p4);
        this.bDoDraw = t.bDoDraw;
    }
}
