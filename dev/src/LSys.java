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
import java.awt.List;
import java.util.*;
import java.io.*;
import java.lang.*;

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
class ParamPanel

Purpose:   This is the panel containing all of the parameters, and allows
	   them to be adjusted through control widgets contained within it
================================================================================*/
class ParamPanel extends Panel {

    public Button b_add, b_del, b_updt, b_apply, b_new;
    public Checkbox cb_strt, cb_hermite, cb_bspline;   /* choices for line shape */
    public CheckboxGroup cbg_Lines;         /* checkbox group of line types */
    public List ch_ptrnspecs;		/* choices for pattern specifications */
    public List lst_rules;
    public TextField tb_axiom, tb_depth, tb_scale, tb_angle,
            tb_rule, tb_ignore, tb_clr;

    public int curIdx;	/* index of item in list currently being edited */
    public boolean b_doApply; 	/* flag whether Apply button has been pressed */
    public boolean b_gotImagePnl=false;	/* flag whether have been given
					   associated drawing panel */
    public LSysPanel pnl_image;

    public Color aColors[];     /* array of colors */

    public Globals glb;         /* globals */


    /* constructor */
    public ParamPanel() {
        Insets lft_spc = new Insets(0,5,0,0);

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
        ClrPanel bPanel = new ClrPanel(aColors);


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



		/*------------
		  color panel
		  ------------*/



        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(15,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(bPanel,c);
        add(bPanel);

        gridbag = new GridBagLayout();
        bPanel.setLayout(gridbag);


        GridBagConstraints c_num = new GridBagConstraints();

        c_num.gridwidth = 1;    /* first in row */
        c_num.fill = GridBagConstraints.HORIZONTAL;
        c_num.weightx = 1;


        c_num.insets = new Insets(5,20,5,0);
        Label label8 = new Label("0");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        c_num.insets= new Insets(5,59,5,0);
        label8 = new Label("5");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        label8 = new Label("10");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        label8 = new Label("15");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        label8 = new Label("20");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        label8 = new Label("25");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);


        label8 = new Label("30");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        label8 = new Label("35");
        gridbag.setConstraints(label8, c_num);
        bPanel.add(label8);

        c_num.insets = new Insets(5,10,5,5);
        tb_clr = new TextField(3);
        gridbag.setConstraints(tb_clr, c_num);
        bPanel.add(tb_clr);



        gridbag = new GridBagLayout();
        c = new GridBagConstraints();
        setFont(new Font("Helvetica", Font.PLAIN, 14));
        lPanel.setLayout(gridbag);

        c.fill = GridBagConstraints.NONE;  /* stretch components left to right */



        c.fill = GridBagConstraints.HORIZONTAL;  /* stretch components left to right */

		/*----------------------
		  LSystems Samples
		  ---------------------*/
        GridBagConstraints c_smpl = new GridBagConstraints();
        c_smpl.gridx = 0;
        c_smpl.gridy = 0;
        c_smpl.gridwidth = GridBagConstraints.REMAINDER;


		/* Rules Label */
        c_smpl.anchor = GridBagConstraints.WEST;
        Label label7 = new Label("Sample LSystems:");
        gridbag.setConstraints(label7,c_smpl);
        lPanel.add(label7);


        GridBagConstraints c_btn = new GridBagConstraints();
        c_btn.gridwidth = 1;
        c_btn.fill = GridBagConstraints.HORIZONTAL;
        c_btn.weightx = 1;

        c_btn.gridx=0;
        c_btn.gridy=1;
        TextField tb_placeholder = new TextField(25);
        gridbag.setConstraints(tb_placeholder, c_btn);
        tb_placeholder.hide();
        lPanel.add(tb_placeholder);
        tb_placeholder.show();
        tb_placeholder.hide();

		/* List of Samples LSystem Patterns */
        c_smpl.fill = GridBagConstraints.HORIZONTAL;
        c_smpl.anchor = GridBagConstraints.WEST;
        c_smpl.gridy = 1;
        ch_ptrnspecs = new List(6,false);
        ch_ptrnspecs.clear();
        gridbag.setConstraints(ch_ptrnspecs, c_smpl);
        lPanel.add(ch_ptrnspecs);
        fillSpecChoices(ch_ptrnspecs);



        c_btn = new GridBagConstraints();
        c_btn.gridwidth = 1;
        c_btn.fill = GridBagConstraints.HORIZONTAL;
        c_btn.weightx = 1;
        c_btn.insets = new Insets (30,0,0,0);


		/* Apply button */
        b_apply = new Button("Apply");
        gridbag.setConstraints(b_apply, c_btn);
        lPanel.add(b_apply);






        gridbag = new GridBagLayout();
        c = new GridBagConstraints();
        setFont(new Font("Helvetica", Font.PLAIN, 14));
        mPanel.setLayout(gridbag);
        c.fill = GridBagConstraints.NONE;  /* stretch components left to right */

		/*----------------------
		  Rule Controls
		  ---------------------*/
        GridBagConstraints c_rule = new GridBagConstraints();
        c_rule.gridx = 0;
        c_rule.insets=lft_spc;
        c_rule.gridwidth = GridBagConstraints.REMAINDER;

		/* Rules Label */
        c_rule.anchor = GridBagConstraints.WEST;
        Label label5 = new Label("Rules:");
        gridbag.setConstraints(label5,c_rule);
        mPanel.add(label5);


		/* List of Rules */
        c_rule.fill = GridBagConstraints.HORIZONTAL;
        c_rule.anchor = GridBagConstraints.WEST;
        lst_rules = new List(4, false);
        gridbag.setConstraints(lst_rules,c_rule);
        mPanel.add(lst_rules);
        lst_rules.clear();

		/* rule edit box */
        c_rule.insets = new Insets(35,5,0,0);
        c_rule.anchor = GridBagConstraints.WEST;
        c_rule.fill = GridBagConstraints.NONE;
        tb_rule = new TextField(27);
        gridbag.setConstraints(tb_rule, c_rule);
        mPanel.add(tb_rule);





		/*------------------
		  Rule Editing Buttons
	   	  (Add, Update, Delete, New)
		  ------------------*/

        c_btn = new GridBagConstraints();
        c_btn.insets=lft_spc;
        c_btn.gridwidth = 1;
        c_btn.fill = GridBagConstraints.HORIZONTAL;
        c_btn.weightx = 1;

		/* rule editing buttons */
        b_add = new Button("Add");
        b_updt = new Button("Update");
        b_del = new Button("Delete");
        b_new = new Button("Clear");


        gridbag.setConstraints(b_add, c_btn);

        gridbag.setConstraints(b_updt, c_btn);

        gridbag.setConstraints(b_del, c_btn);

        c_btn.gridwidth = GridBagConstraints.REMAINDER;  /* last-in-row */
        gridbag.setConstraints(b_new, c_btn);


        mPanel.add(b_add);
        mPanel.add(b_updt);
        mPanel.add(b_del);
        mPanel.add(b_new);


        gridbag = new GridBagLayout();
        c = new GridBagConstraints();
        setFont(new Font("Helvetica", Font.PLAIN, 14));
        rPanel.setLayout(gridbag);

        c.fill = GridBagConstraints.NONE;  /* don't stretch components */


        cbg_Lines = new CheckboxGroup();


		/*-------------------
		  Line Checkboxes
		  ------------------*/
        GridBagConstraints c_line = new GridBagConstraints();
        c_line.gridx = 0;
        c_line.anchor = GridBagConstraints.WEST;
        c_line.gridwidth = GridBagConstraints.REMAINDER; /* last-in-row */



		/* Straight Line Checkbox*/

        c_line.insets= new Insets(20,50,0,0);
        cb_strt = new Checkbox("Straight Lines",cbg_Lines,true);
        gridbag.setConstraints(cb_strt, c_line);
        rPanel.add(cb_strt);


		/* Hermite Curve Checkbox*/

        c_line.insets= new Insets(0,50,0,0);
        cb_hermite = new Checkbox("Hermite Curved Lines",cbg_Lines,false);
        gridbag.setConstraints(cb_hermite, c_line);
        rPanel.add(cb_hermite);


		/* BSpline Curve Checkbox*/
        cb_bspline = new Checkbox("B-Spline Curved Lines",cbg_Lines,false);
        c_line.insets= new Insets(0,50,12,0);
        gridbag.setConstraints(cb_bspline, c_line);
        rPanel.add(cb_bspline);




		/*-------------------
		  Parameter Text Boxes

		  Axiom, Ignore, Depth, Angle, Scale
		  ------------------*/

        GridBagConstraints c_textb = new GridBagConstraints();

		/* Axiom */
        c_textb.gridwidth = 1;    /* first in row */
        c_textb.anchor = GridBagConstraints.WEST;
        Label label1 = new Label("Axiom: ");
        gridbag.setConstraints(label1, c_textb);
        rPanel.add(label1);


        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        c_textb.gridwidth = GridBagConstraints.REMAINDER;/* last-in-row */
        tb_axiom = new TextField(26);
        gridbag.setConstraints(tb_axiom, c_textb);
        rPanel.add(tb_axiom);


		/* Ignore */
        c_textb.gridwidth = 1;    /* first in row */
        c_textb.anchor = GridBagConstraints.WEST;
        Label label6 = new Label("Ignore: ");
        gridbag.setConstraints(label6, c_textb);
        rPanel.add(label6);

        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        c_textb.gridwidth = GridBagConstraints.REMAINDER;/* last-in-row */
        tb_ignore = new TextField(26);
        gridbag.setConstraints(tb_ignore, c_textb);
        rPanel.add(tb_ignore);


		/* Depth */
        c_textb.gridwidth = 1;         /* first in row */
        c_textb.anchor = GridBagConstraints.WEST;
        Label label2 = new Label("Depth:");
        gridbag.setConstraints(label2, c_textb);
        rPanel.add(label2);


        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        tb_depth = new TextField(1);
        gridbag.setConstraints(tb_depth, c_textb);
        rPanel.add(tb_depth);


		/* Angle */
        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        Label label3 = new Label("Angle:");
        gridbag.setConstraints(label3, c_textb);
        rPanel.add(label3);


        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        tb_angle = new TextField(3);
        gridbag.setConstraints(tb_angle, c_textb);
        rPanel.add(tb_angle);


		/* Scale */
        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        Label label4 = new Label("Scale:");
        gridbag.setConstraints(label4, c_textb);
        rPanel.add(label4);


        c_textb.anchor = GridBagConstraints.WEST;  /* reset */
        tb_scale = new TextField(3);
        gridbag.setConstraints(tb_scale, c_textb);
        rPanel.add(tb_scale);



        lPanel.show();
        rPanel.show();
        mPanel.show();
        bPanel.show();

        tb_placeholder.hide();


        ch_ptrnspecs.select(0);
        changeSpecs(ch_ptrnspecs.getSelectedIndex());


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
            if (e.target==b_add) {
                lst_rules.addItem(tb_rule.getText());
                tb_rule.setText("");
            } else if (e.target==b_del) {
                lst_rules.delItem(curIdx);
                tb_rule.setText("");
            } else if (e.target==b_updt) {
                lst_rules.replaceItem(tb_rule.getText(),curIdx);
                tb_rule.setText("");
            } else if (e.target==b_apply) {
                b_doApply=true;
				/* apply the most recent control panel settings
				   to redraw the image in the related image
				   panel */
                if (b_gotImagePnl==true) {
                    pnl_image.repaint();
                }

            } else if (e.target==b_new) {
				/* clear fields for new LSystems specification */
                lst_rules.clear();
                tb_rule.setText("");
                tb_axiom.setText("");
                tb_depth.setText("");
                tb_scale.setText("");
                tb_angle.setText("");
                tb_ignore.setText("");
                tb_clr.setText("");
                b_doApply=false;
				/* clear the image panel */
                if (b_gotImagePnl==true) {
                    pnl_image.repaint();
                }


            }
            return(true);
        } else if (e.target instanceof List) {
            if (e.target==lst_rules) {
                tb_rule.setText(lst_rules.getSelectedItem());
                curIdx = lst_rules.getSelectedIndex();
                return(true);
            } else if (e.target==ch_ptrnspecs) {
                changeSpecs(ch_ptrnspecs.getSelectedIndex());

            }
        }


        return(false);  /* return false for unprocessed events */


    }



    public void paint(Graphics g) {


    }


	/*======================================================================
	Method:  fillSpecChoices
	Purpose: fills in the LSystem Sample list box with the names of some
		 predefined LSystems pattern choices


	Params:  ChoiceMenu -- the listbox object to fill
	======================================================================*/

    public void fillSpecChoices(List ChoiceMenu) {

        ChoiceMenu.addItem("Quadratic Koch Island 1 pg. 13"); /* pg. 13 */
        ChoiceMenu.addItem("Quadratic Koch Island 2 pg. 14"); /* pg. 14 */
        ChoiceMenu.addItem("Island & Lake Combo. pg. 15");    /* pg.15 */
        ChoiceMenu.addItem("Koch Curve A pg. 16");
        ChoiceMenu.addItem("Koch Curve B pg. 16");
        ChoiceMenu.addItem("Koch Curve C pg. 16");
        ChoiceMenu.addItem("Koch Curve D pg. 16");
        ChoiceMenu.addItem("Koch Curve E pg. 16");
        ChoiceMenu.addItem("Koch Curve F pg. 16");
        ChoiceMenu.addItem("Mod of Snowflake pg. 14");
        ChoiceMenu.addItem("Dragon Curve pg. 17");
        ChoiceMenu.addItem("Hexagonal Gosper Curve pg. 19");
        ChoiceMenu.addItem("Sierpinski Arrowhead pg. 19");
        ChoiceMenu.addItem("Peano Curve pg. 18");
        ChoiceMenu.addItem("Hilbert Curve pg. 18");
        ChoiceMenu.addItem("Approx of Sierpinski pg. 18");
        ChoiceMenu.addItem("Tree A pg. 25");
        ChoiceMenu.addItem("Tree B pg. 25");
        ChoiceMenu.addItem("Tree C pg. 25");
        ChoiceMenu.addItem("Tree D pg. 25");
        ChoiceMenu.addItem("Tree E pg. 25");
        ChoiceMenu.addItem("Tree B pg. 43");
        ChoiceMenu.addItem("Tree C pg. 43");
        ChoiceMenu.addItem("Spiral Tiling pg. 70");
        ChoiceMenu.addItem("BSpline Triangle pg. 20");
        ChoiceMenu.addItem("Snake Kolam pg. 72");
        ChoiceMenu.addItem("Anklets of Krishna pg. 73");

		/*-----------
		  Color examples
		  -----------*/
        ChoiceMenu.addItem("Color1, Koch Curve B");
        ChoiceMenu.addItem("Color2, Koch Curve B");
        ChoiceMenu.addItem("Color X, Spiral Tiling");
        ChoiceMenu.addItem("Color Center, Spiral Tiling");
        ChoiceMenu.addItem("Color Spokes, Spiral Tiling");
        ChoiceMenu.addItem("Color, Quad Koch Island 1");
        ChoiceMenu.addItem("Color, Tree E");
        ChoiceMenu.addItem("Color, Mod of Snowflake");
        ChoiceMenu.addItem("Color, Anklets of Krishna");
        ChoiceMenu.addItem("Color, Snake Kolam");

        ChoiceMenu.addItem("Simple Branch");


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


	/*======================================================================
	Method:   changeSpecs
	Purpose:  based on the current choice specification, alter the values
		  of the widgets in the control panel
	======================================================================*/

    public void changeSpecs(int Idx) {

        tb_rule.setText("");
        b_doApply=false;
		/* clear the image panel */
        if (b_gotImagePnl==true) {
            pnl_image.repaint();
        }


        switch (Idx){
            case 0:
			/* quadratic Koch Island 1 pg. 13 */
                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("2");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * -->  F+F-F-FF+F+F-F");

                break;


            case 1:   /* Quadratic Koch Island 2 pg. 14 */

                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("2");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F-FF+FF+F+F-F-FF+F+F-F-FF-FF+F");
                break;

            case 2:   /* Island & Lake Combo. pg. 15 */


                tb_clr.setText("16");
                tb_axiom.setText("F-F-F-F");
                tb_ignore.setText("Ff+-");
                tb_depth.setText("2");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F-f+FF-F-FF-Ff-FF+f-FF+F+FF+Ff+FFF");
                lst_rules.addItem("* <f> * --> ffffff");
                break;

            case 3:	  /* Koch Curve A pg. 16 */


                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+F+F+F+F+F-F");
                break;

            case 4:	  /* Koch Curve B pg. 16 */

                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+F+F+F+FF");


                break;

            case 5:	  /* Koch Curve C pg. 16 */


                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("3");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+F-F+F+FF");
                break;


            case 6:   /* Koch Curve D pg. 16 */


                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+F++F+F");
                break;

            case 7:	  /* Koch Curve E pg. 16 */


                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+");
                tb_depth.setText("5");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F+FF++F+F");
                break;

            case 8:   /* Koch Curve F pg. 16 */


                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F+F-F+F+F");
                break;

            case 9:  /* Mod of snowflake pg. 14*/


                tb_clr.setText("16");
                tb_axiom.setText("+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F-F+F+F-F");

                break;

            case 10:  /* Dragon Curve pg. 17 */


                tb_clr.setText("16");
                tb_axiom.setText("Fl");
                tb_ignore.setText("F+-");
                tb_depth.setText("14");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <l> * --> l+rF+");
                lst_rules.addItem("* <r> * --> -Fl-r");
                break;

            case 11:  /* Hexagonal Gosper Curve pg. 19 */


                tb_clr.setText("16");
                tb_axiom.setText("XF");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("90");
                tb_angle.setText("6");
                lst_rules.clear();
                lst_rules.addItem("* <XF> * --> XF+YF++YF-XF--XFXF-YF+");
                lst_rules.addItem("* <YF> * --> -XF+YFYF++YF+XF--XF-YF");
                break;

            case 12:  /* Sierpinski Arrowhead pg. 19 */


                tb_clr.setText("16");
                tb_axiom.setText("YF");
                tb_ignore.setText("F+-");
                tb_depth.setText("6");
                tb_scale.setText("100");
                tb_angle.setText("6");
                lst_rules.clear();
                lst_rules.addItem("* <XF> * --> YF+XF+YF");
                lst_rules.addItem("* <YF> * --> XF-YF-XF");
                break;

            case 13:  /* Peano Curve pg. 18 */


                tb_clr.setText("16");
                tb_axiom.setText("X");
                tb_ignore.setText("F+-");
                tb_depth.setText("3");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XFYFX+F+YFXFY-F-XFYFX");
                lst_rules.addItem("* <Y> * --> YFXFY-F-XFYFX+F+YFXFY");
                break;

            case 14:  /* Hilbert Curve pg. 18 */


                tb_clr.setText("16");
                tb_axiom.setText("X");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> -YF+XFX+FY-");
                lst_rules.addItem("* <Y> * --> +XF-YFY-FX+");
                break;

            case 15:  /* Approx of Sierpinski pg. 18 */

                tb_clr.setText("16");
                tb_axiom.setText("F+XF+F+XF");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XF-F+F-XF+F+XF-F+F-X");
                break;

            case 16:  /* Tree A pg. 25 */


                tb_clr.setText("16");
                tb_axiom.setText("F");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("14");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F[+F]F[-F]F");
                break;

            case 17:  /* Tree B pg. 25 */


                tb_clr.setText("16");
                tb_axiom.setText("X");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("16");
                lst_rules.clear();
                lst_rules.addItem(" * <X> * --> F-[[X]+X]+F[+FX]-X");
                lst_rules.addItem(" * <F> * --> FF");
                break;


            case 18:  /* Tree C pg. 25 */


                tb_clr.setText("16");
                tb_axiom.setText("Y");
                tb_ignore.setText("F+-");
                tb_depth.setText("6");
                tb_scale.setText("100");
                tb_angle.setText("14");
                lst_rules.clear();
                lst_rules.addItem(" * <Y> * --> YFX[+Y][-Y]");
                lst_rules.addItem(" * <X> * --> X[-FFF][+FFF]FX");
                break;

            case 19:  /* Tree D pg. 25 */


                tb_clr.setText("16");
                tb_axiom.setText("F");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("16");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+[+F-F-F]-[-F+F+F]");
                break;

            case 20:  /* Tree E pg. 25 */


                tb_clr.setText("16");
                tb_axiom.setText("X");
                tb_ignore.setText("F+-");
                tb_depth.setText("7");
                tb_scale.setText("100");
                tb_angle.setText("18");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> F[+X]F[-X]+X");
                lst_rules.addItem("* <F> * --> FF");

                break;

            case 21:  /* Tree B pg. 43 */
                tb_clr.setText("16");
                tb_axiom.setText("F1F1F1");
                tb_ignore.setText("F+-");
                tb_depth.setText("30");
                tb_scale.setText("100");
                tb_angle.setText("16");
                lst_rules.clear();
                lst_rules.addItem("0 <0> 0 --> 1");
                lst_rules.addItem("0 <0> 1 --> 1[-F1F1]");
                lst_rules.addItem("0 <1> 0 --> 1");
                lst_rules.addItem("0 <1> 1 --> 1");
                lst_rules.addItem("1 <0> 0 --> 0");
                lst_rules.addItem("1 <0> 1 --> 1F1");
                lst_rules.addItem("1 <1> 0 --> 1");
                lst_rules.addItem("1 <1> 1 --> 0");
                lst_rules.addItem("* <-> * --> +");
                lst_rules.addItem("* <+> * --> -");
                break;


            case 22:  /* Tree C pg. 43 */
                tb_clr.setText("16");
                tb_axiom.setText("F1F1F1");
                tb_ignore.setText("F+-");
                tb_depth.setText("26");
                tb_scale.setText("100");
                tb_angle.setText("14");
                lst_rules.clear();
                lst_rules.addItem("0 <0> 0  --> 0");
                lst_rules.addItem("0 <0> 1 --> 1");
                lst_rules.addItem("0 <1> 0 --> 0");
                lst_rules.addItem("0 <1> 1 --> 1[+F1F1]");
                lst_rules.addItem("1 <0> 0 --> 0");
                lst_rules.addItem("1 <0> 1 --> 1F1");
                lst_rules.addItem("1 <1> 0 --> 0");
                lst_rules.addItem("1 <1> 1--> 0");
                lst_rules.addItem("* <-> * --> +");
                lst_rules.addItem("* <+> * --> -");
                break;


            case 23:  /* Spiral Tiling pg. 70 */
                tb_clr.setText("16");
                tb_axiom.setText("AAAA");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("24");
                lst_rules.clear();
                lst_rules.addItem("* <A> * --> X+X+X+X+X+X+");
                lst_rules.addItem("* <X> * --> [F+F+F+F[---X-Y]+++++F++++++++F-F-F-F]");
                lst_rules.addItem("* <Y> * --> [F+F+F+F[---Y]+++++F++++++++F-F-F-F]");

                break;


            case 24:  /* BSpline Triangle pg. 20 */
                tb_clr.setText("16");
                tb_axiom.setText("F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("3");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> F-F+F");

                break;

            case 25:  /* Snake Kolam pg. 72 */
                tb_clr.setText("16");
                tb_axiom.setText("F+XF+F+XF");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XF-F-F+XF+F+XF-F-F+X");

                break;


            case 26:  /* Anklets of Krishna pg. 73 */
                tb_clr.setText("16");
                tb_axiom.setText("-X--X");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("8");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XFX--XFX");

                break;

            case 27: /* Color, Koch Curve B */

                tb_clr.setText("16");
                tb_axiom.setText("F+F+F+F");
                tb_ignore.setText("F+");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> FF+F+;;;;;F:::::+F+FF");

                break;

            case 28: /* Color, Koch Curve B */

                tb_clr.setText("16");
                tb_axiom.setText("###F+F+F+F");
                tb_ignore.setText("F+");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * -->;FF+F+F+F+FF");

                break;


            case 29: /* Color X, Spiral Tiling */
                tb_clr.setText("16");
                tb_axiom.setText("AAAA");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("24");
                lst_rules.clear();
                lst_rules.addItem("* <A> * --> ;;;;;;;;X::::::::+X+X+X+X+X+");
                lst_rules.addItem("* <X> * --> [F+F+F+F[---X-Y]+++++F++++++++F-F-F-F]");
                lst_rules.addItem("* <Y> * --> [F+F+F+F[---Y]+++++F++++++++F-F-F-F]");
                break;

            case 30: /* Color Center, Spiral Tiling */
                tb_clr.setText("16");
                tb_axiom.setText("AAAA");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("24");
                lst_rules.clear();
                lst_rules.addItem("* <A> * --> X+X+X+X+X+X+");
                lst_rules.addItem("* <X> * --> [;;;;;F+F+F+F:::::[---X-Y]+++++F++++++++F-F-F-F]");
                lst_rules.addItem("* <Y> * --> [F+F+F+F[---Y]+++++F++++++++F-F-F-F]");
                break;

            case 31: /* Color Spokes, Spiral Tiling */
                tb_clr.setText("16");
                tb_axiom.setText("AAAA");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("24");
                lst_rules.clear();
                lst_rules.addItem("* <A> * --> X+X+X+X+X+X+");
                lst_rules.addItem("* <X> * --> [F+F+F+F[---X-Y]+++++F++++++++;;;;F-F-F-F::::]");
                lst_rules.addItem("* <Y> * --> [F+F+F+F[---Y]+++++F++++++++F-F-F-F]");
                break;

            case 32: /* Color, Quad Koch Island 1 */
			/* quadratic Koch Island 1 pg. 13 */
                tb_clr.setText("16");
                tb_axiom.setText("###F+F+F+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("2");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * -->  ;F+F-F-FF+F+F-F");

                break;

            case 33: /* Color, Tree E */
                tb_clr.setText("16");
                tb_axiom.setText("X");
                tb_ignore.setText("F+-");
                tb_depth.setText("7");
                tb_scale.setText("100");
                tb_angle.setText("18");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> F[+X]F[-X]+X");
                lst_rules.addItem("* <F> * --> ;;FF::");

                break;


            case 34: /* Color, Mod of Snowflake */
                tb_clr.setText("16");
                tb_axiom.setText("###+F");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("100");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <F> * --> ;F-F+F+F-F");

                break;

            case 35: /* Color, Anklets of Krishna */
                tb_clr.setText("16");
                tb_axiom.setText("-X--X");
                tb_ignore.setText("F+-");
                tb_depth.setText("5");
                tb_scale.setText("100");
                tb_angle.setText("8");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XFX--X;;;;;;F::::::X");
                break;

            case 36: /* Color, Snake Kolam */
                tb_clr.setText("16");
                tb_axiom.setText("F+XF+F+;;;XF:::");
                tb_ignore.setText("F+-");
                tb_depth.setText("4");
                tb_scale.setText("90");
                tb_angle.setText("4");
                lst_rules.clear();
                lst_rules.addItem("* <X> * --> XF-F-F+XF+F+XF-F-F+X");

                break;

            case 37: /* Simple Branch */
                tb_clr.setText("16");
                tb_axiom.setText("FFF[-FFF][--FFF][FFF][+FFF][++FFF]");
                tb_ignore.setText("F+-");
                tb_depth.setText("1");
                tb_scale.setText("90");
                tb_angle.setText("8");
                lst_rules.clear();

                break;




            default:
                tb_axiom.setText("");
                tb_ignore.setText("");
                tb_depth.setText("");
                tb_scale.setText("");
                tb_angle.setText("");
                lst_rules.clear();
                break;

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
            b = pixgen.ProcessLSys(pnl_Specs,g,height,width);

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
    public int clrIdx;   			/* index into array of colors */


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
    public boolean ProcessLSys(ParamPanel pnl_specs, Graphics g,double pnl_ht, double pnl_wd){

        boolean bOk;


        startpos.horiz = 0;
        startpos.vert = 0;


        bOk = GetSpecs(pnl_specs);

        if (bOk == false) {
            return(false);
        }


        bOk = Derive();


        bOk = Interpret(g, pnl_ht, pnl_wd, pnl_specs.aColors);

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

    public boolean GetSpecs(ParamPanel pnl_specs){
        int iPred, iPost, iSucc;	/* separator indexes in rule */
        String sRule;			/* holds rule being parsed */
        int i;


        i=0;



        iNumRules = pnl_specs.lst_rules.countItems();
        this.aRules = new Production[iNumRules];

        if (pnl_specs.cbg_Lines.getCurrent()==pnl_specs.cb_strt) {
            Specs.nLnType = glb.LN_STRT;
        } else if (pnl_specs.cbg_Lines.getCurrent()==pnl_specs.cb_hermite) {
            Specs.nLnType = glb.LN_HERMITE;
        } else {
            Specs.nLnType = glb.LN_BSPLINE;
        }


        Specs.axiom = pnl_specs.tb_axiom.getText();
        if (Specs.axiom == ""){
            return(false);
        }
        Ignore = pnl_specs.tb_ignore.getText();
        try {
            Specs.angle = Integer.parseInt(pnl_specs.tb_angle.getText());
        } catch (NumberFormatException e) {

            System.out.println("Error!  Angle must be an integer.");
            Specs.angle = 4;
            return(false);
        }
        try {
            Specs.scale = Integer.parseInt(pnl_specs.tb_scale.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error!  Scale must be an integer.");
            Specs.scale = 50;
            return(false);
        }

        try {
            Specs.depth = Integer.parseInt(pnl_specs.tb_depth.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error!  Depth must be an integer.");
            Specs.depth = 3;
            return(false);
        }

        try {
            Specs.start_clr = Integer.parseInt(pnl_specs.tb_clr.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error!  Color must be an integer.");
            Specs.start_clr = 0;
            return(false);
        }

        if (Specs.start_clr>glb.MAXCOLORS-1) {
            Specs.start_clr = glb.MAXCOLORS-1;
        } else if (Specs.start_clr <0) {
            Specs.start_clr = 0;
        }


        for (i = 0; i < iNumRules; i++) {
            sRule = pnl_specs.lst_rules.getItem(i);


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

        if (Specs.nLnType==glb.LN_HERMITE) {
            ct.bDoDraw=false;
            ct.p2.getvals(curpos);
        } else if (Specs.nLnType==glb.LN_BSPLINE) {
            ct.bDoDraw=true;
            ct.p1.getvals(curpos);
            ct.p2.getvals(curpos);
            ct.p3.getvals(curpos);
            ct.p4.getvals(curpos);

			/* initialize storage of first 3 points in curve */
            aFirstPts[0] = new Turtle();
            aFirstPts[1] = new Turtle();
            aFirstPts[2] = new Turtle();
            aFirstPts[0].getvals(curpos);
            iPtsIdx = 1;
        }



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

                        if (bDoDraw==true) {
						/* draw first and last curve segments of previous
						   curve */
                            if (Specs.nLnType==glb.LN_BSPLINE) {


                            }
                        }

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




                        if (Specs.nLnType == glb.LN_HERMITE) {
                            if (ct.bDoDraw==true) {

                                ct.p4.x = curpos.x-ct.p2.x;
                                ct.p4.y = curpos.y-ct.p2.y;

                                DrawHermite(ct,iStepSz,g);

							/*ct.bDoDraw=true;*/
                                ct.p2.getvals(ct.p3);
                                ct.p3.getvals(curpos);
                                ct.p1.getvals(ct.p4);

                            } else {
						/*------------------------------------------
						  We have got only the start position for
						  drawing a segment, so we can't draw yet
						  ---------------------------------------*/
                                ct.bDoDraw=true;
                                ct.p3.getvals(curpos);
                                ct.p1.x = curpos.x-ct.p2.x;
                                ct.p1.y = curpos.y-ct.p2.y;




                            }
                        } else if (Specs.nLnType == glb.LN_BSPLINE) {
						/*------------------------
						  draw BSpline curve
						  -----------------------*/
                            ct.p1.getvals(ct.p2);
                            ct.p2.getvals(ct.p3);
                            ct.p3.getvals(ct.p4);
                            ct.p4.getvals(curpos);
						/*-------------
						  don't draw curve for first 3 points, just
						  store their locations (for generating
						  the first curve segment later)
						 -------------*/
                            if (iPtsIdx<3) {
                                aFirstPts[iPtsIdx].getvals(curpos);
                                iPtsIdx++;
                            } else {

                                DrawBSpline(ct,iStepSz,g);
                            }
                        } else if (Specs.nLnType == glb.LN_STRT) {
                            drawWideLine(prevpos, curpos, g);
                        }



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
                        if (Specs.nLnType==glb.LN_BSPLINE) {
                            DrawFirstNLast(ct, aFirstPts,g,iStepSz,iPtsIdx);
                        }


                        if (Specs.nLnType==glb.LN_HERMITE) {
                            ct.bDoDraw=false;
                            ct.p2.getvals(curpos);
                        } else if (Specs.nLnType==glb.LN_BSPLINE) {
                            ct.bDoDraw=true;
                            ct.p1.getvals(curpos);
                            ct.p2.getvals(curpos);
                            ct.p3.getvals(curpos);
                            ct.p4.getvals(curpos);

						/* initialize storage of first 3 points in curve */
                            aFirstPts[0].getvals(curpos);
                            iPtsIdx = 1;

                        }

                        prevpos.getvals(curpos);  /* copy position*/

                    } else {
                        UpdateBox(curpos);

                    }


                    break;
            }


        }
        if (bDoDraw==true) {

			/* draw first and last curve segments of previous
			   curve */
            if (Specs.nLnType==glb.LN_BSPLINE) {
                DrawFirstNLast(ct, aFirstPts,g,iStepSz,iPtsIdx);
            }
        }

        return(true);

    }


    /*----------------------------------------------------------------------
    Method:   DrawFirstNLast
    Purpose:  Draw the first and last BSpline curve segments in a total
          curve.

    Algorithm:	If the start and end of a curve meet up then we can
            use the points at the start and end to control the
            curve segment.

    -----------------------------------------------------------------------*/
    void DrawFirstNLast(CurveTurtle final_ct, Turtle aFirstPts[], Graphics g, 				int iStepSz, int iPtsIdx)
    {


        if ((Math.floor(final_ct.p4.x) == Math.floor(aFirstPts[0].x))&&(Math.floor(final_ct.p4.y) == Math.floor(aFirstPts[0].y))) {
		/*---------------
		  start of curve joins with end of curve
		  --------------*/
			/* Draw end segment of curve */
            final_ct.p1.getvals(final_ct.p2);
            final_ct.p2.getvals(final_ct.p3);
            final_ct.p3.getvals(final_ct.p4);
            final_ct.p4.getvals(aFirstPts[1]);
            DrawBSpline(final_ct,iStepSz,g);

			/* Draw start segment of curve */
            final_ct.p1 = final_ct.p2;
            final_ct.p2 = final_ct.p3;
            final_ct.p3 = final_ct.p4;
            final_ct.p4 = aFirstPts[2];
            DrawBSpline(final_ct,iStepSz,g);


        } else {


		/*--------------
		  start of curve is independent of end of curve
		  -------------*/
			/* Draw end segment of curve, depending on
			   last point as last TWO control points */
            final_ct.p1.getvals(final_ct.p2);
            final_ct.p2.getvals(final_ct.p3);
            final_ct.p3.getvals(final_ct.p4);
            final_ct.p4 = final_ct.p4;

            DrawBSpline(final_ct,iStepSz,g);

			/* only draw first curve segment if more than one
			   forward movement ('F') was encountered */
            if (iPtsIdx>2) {

				/* Draw start segment of curve, depending on
				   first point as first TWO control points */
                final_ct.p1 = aFirstPts[0];
                final_ct.p2 = aFirstPts[0];
                final_ct.p3 = aFirstPts[1];
                final_ct.p4 = aFirstPts[2];
                DrawBSpline(final_ct,iStepSz,g);
            }


        }


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


	/*---------------------------------------------------------------------
	Method:   DrawHermite

	Purpose:  Draw a Hermite curve between two points, given the
		  start point, end point, start tangent and end tangent

	Param:    ct      --  curved turtle specifying start and end points
		              and tangent vectors
			      p1 - start tangent vector
			      p2 - start point
			      p3 - end point
			      p4 - end tangent vector

		  numParts -- number of partitions for discretizing curve

	----------------------------------------------------------------------*/

    void DrawHermite(CurveTurtle ct, int numParts, Graphics g){

        int i;
        double t;
        double t_inc;
        Turtle Qcur, Qprev;   /* positions on curve as drawn */


        t_inc = (double)(1/(((double)numParts-1)));

        Qcur = new Turtle();
        Qprev = new Turtle();

        Qprev.getvals(ct.p2);


        for (i=1,t=t_inc;i<numParts-1;i++,t+=t_inc){


            Qcur.x = ((2*Math.pow(t,3)-3*Math.pow(t,2)+1)*ct.p2.x) +
                    ((-2*Math.pow(t,3)+3*Math.pow(t,2))*ct.p3.x) +
                    ((Math.pow(t,3)-2*Math.pow(t,2)+t)*ct.p1.x) +
                    ((Math.pow(t,3)-Math.pow(t,2))*ct.p4.x);

            Qcur.y = ((2*Math.pow(t,3)-3*Math.pow(t,2)+1)*ct.p2.y) +
                    ((-2*Math.pow(t,3)+3*Math.pow(t,2))*ct.p3.y) +
                    ((Math.pow(t,3)-2*Math.pow(t,2)+t)*ct.p1.y) +
                    ((Math.pow(t,3)-Math.pow(t,2))*ct.p4.y);


            g.drawLine((int)Qprev.x, (int)Qprev.y, (int) Qcur.x, (int) Qcur.y);
            Qprev.getvals(Qcur);



        }

        drawWideLine(Qprev,ct.p3,g);


    }



	/*---------------------------------------------------------------------
	Method:   DrawBSpline

	Purpose:  Draw a BSpline curve based on 4 given control points


	Param:    ct      --  curved turtle specifying control points:
				ct.p1 = point 1
				ct.p2 = point 2
				ct.p3 = point 3
				ct.p4 = point 4

		  numParts -- number of partitions for discretizing curve

	----------------------------------------------------------------------*/

    void DrawBSpline(CurveTurtle ct, int numParts, Graphics g){

        int i;
        double t;
        double t_inc;
        Turtle Qcur, Qprev;   /* positions on curve as drawn */


        Turtle p0 = new Turtle();
        Turtle p1 = new Turtle();
        Turtle p2 = new Turtle();
        Turtle p3 = new Turtle();

        p0.getvals(ct.p1);
        p1.getvals(ct.p2);
        p2.getvals(ct.p3);
        p3.getvals(ct.p4);


        t_inc = (double)(1/(((double)numParts-1)));
        Qcur = new Turtle();
        Qprev = new Turtle();


        t=0;
        Qprev.x = ((Math.pow(1-t,3))/6*p0.x) +
                ((3*Math.pow(t,3)-6*Math.pow(t,2)+4)/6*p1.x) +
                ((-3*Math.pow(t,3)+3*Math.pow(t,2)+3*t+1)/6*p2.x)+
                ((Math.pow(t,3))/6*p3.x);

        Qprev.y = ((Math.pow(1-t,3))/6*p0.y) +
                ((3*Math.pow(t,3)-6*Math.pow(t,2)+4)/6*p1.y) +
                ((-3*Math.pow(t,3)+3*Math.pow(t,2)+3*t+1)/6*p2.y)+
                ((Math.pow(t,3))/6*p3.y);


		/*----------------------------------------------------
		  draw the curve, with a total of "numParts" different
		  segments drawn in the curve
		  --------------------------------------------------*/
        for (i=1,t=t_inc;i<numParts;i++,t+=t_inc){

            Qcur.x = ((Math.pow(1-t,3))/6*p0.x) +
                    ((3*Math.pow(t,3)-6*Math.pow(t,2)+4)/6*p1.x) +
                    ((-3*Math.pow(t,3)+3*Math.pow(t,2)+3*t+1)/6*p2.x)+
                    ((Math.pow(t,3))/6*p3.x);

            Qcur.y = ((Math.pow(1-t,3))/6*p0.y) +
                    ((3*Math.pow(t,3)-6*Math.pow(t,2)+4)/6*p1.y) +
                    ((-3*Math.pow(t,3)+3*Math.pow(t,2)+3*t+1)/6*p2.y)+
                    ((Math.pow(t,3))/6*p3.y);



            drawWideLine(Qprev, Qcur, g);


            Qprev.getvals(Qcur);



        }



    }



}

/*========================================================================
  Class Globals

  This class contains globals to be shared among all of the objects

  =======================================================================*/
abstract class Globals extends Object {
    static final int MAXCOLORS = 36;      /* max number of colors in array */

    static final int LN_STRT=0;           /* line types */
    static final int LN_HERMITE=1;
    static final int LN_BSPLINE=2;



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
