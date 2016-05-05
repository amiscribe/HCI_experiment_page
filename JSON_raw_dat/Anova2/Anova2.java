// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 06/04/2005 //5:50:11 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for //new version!
// Decompiler options: packimports(3) 
// Source File Name:   Anova.java

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Anova2 - a Java utility to perform an analysis of variance (ANOVA) on a table of data read from a
 * file.
 * <p>
 * 
 * Five experiment designs are supported:
 * <p>
 * 
 * <ul>
 * <li>One-way with one within-subjects factor
 * <li>One-way with one between-subjects factor
 * <li>Two-way with two within-subjects factors
 * <li>Two-way with one within-subjects factor and one between-subjects factor
 * <li>Three-way with two within-subjects factors and one between-subjects factor
 * </ul>
 * 
 * Note on terminology: A "within-subjects factor" is often called a "repeated-measures factor". A
 * "factor" is often called an "independent variable". The levels of a factor are often called
 * "test conditions".
 * <p>
 * 
 * The data must be organized as a <i>p</i> &times; <i>n</i> matrix. <i>p</i> is the number of
 * participants (one per row) and <i>n</i> is the number of within-subjects test conditions (one per
 * column). Each entry in the matrix contains a measurement on the behaviour of interest (e.g., task
 * completion time or error rate).
 * <p>
 * 
 * For designs with one within-subjects factor, <i>n</i> is the number of levels of the factor. For
 * designs with two within-subjects factors, <i>n</i> is the product of the number of levels of each
 * factor. For example, a two-factor experiment with repeated measures on 15 participants, having 2
 * levels on the first factor and 3 levels on the second, requires a data file with 15 rows and 2
 * &times; 3 = 6 columns. In total, there are 6 test conditions. Such an experiment is called a
 * "2 &times; 3 within-subjects design".
 * <p>
 * 
 * If two within-subjects factors are used, the nesting of data is important. The columns are
 * ordered with the levels of the second factor nested within the levels of the first factor. As an
 * example, for a 2 &times; 3 design, the respective order of the data from columns one to six is
 * F1L1-F2L1, F1L1-F2L2, F1L1-F2L3, F1L2-F2L1, F1L2-F2L2, F1L2-F2L3, where F = factor and L = level.
 * The following figure illustrates:
 * <p>
 * 
 * <blockquote><img src="Anova2-0.gif"> </blockquote>
 * <p>
 * 
 * If a between-subjects factor is used, the factor appears as an additional column of nominal data.
 * This is typically a group identifier, and is used, for example, if the participants were divided
 * into groups to counterbalance the order of administering the within-subjects test conditions. A
 * between-subjects factor could also be used for other circumstances (e.g., gender or handedness).
 * <p>
 * 
 * If a between-subjects factor is used, the same number of participants is required in each group.
 * <p>
 * 
 * Invocation (usage message if invoked without arguments):
 * <p>
 * 
 * <blockquote><a href="Anova2-6.jpg"><img src="Anova2-6.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * If a between-subjects factor is present, the <code>f3</code> argument is an integer corresponding
 * to the number of groups. <code>f3</code>, if present, must divide evenly into <code>p</code>. For
 * example, <code>p</code> = 15, <code>f1</code> = 3, and <code>f3</code> = 3 means the experiment
 * involved 15 participants and a within-subjects factor with 3 levels. There will be 3 columns of
 * data for the levels of the <code>f1</code> factor. Counterbalancing was used with participants
 * divided into 3 groups with 5 participants/group. (Here, "group" is treated like a
 * between-subjects factor.) The group identifier (which can be a number or label) is in the 4th
 * column. The following figure illustrates:
 * <p>
 * 
 * <blockquote> <img src="Anova2-0a.gif"> </blockquote>
 * <p>
 * 
 * If a between-subjects factor is the only factor (e.g., gender), then the data file contains just
 * two columns, one for the data and one to identify the groups.
 * <p>
 * 
 * If any factor is not present, its command-line argument is replaced with "<code>.</code>".
 * <p>
 * 
 * Four optional arguments are supported:
 * <p>
 * 
 * <ul>
 * <li><b>-a option</b> &ndash; The "<code>-a</code>" option produces the ANOVA table. The default
 * is no output, so make sure either the <code>-a</code>, <code>-d</code>, or <code>-m</code> option
 * is present.
 * <p>
 * 
 * <li><b>-d option</b> &ndash; The "<code>-d</code>" option produces debug output showing the
 * original data as well as the means, sums of squares, mean squares, degrees of freedom, <i>F</i>
 * statistics, and <i>p</i> for the <i>F</i> statistics, for all effects. Output is produced even
 * for effects that do not exist, so some values may appear as <code>NaN</code> or
 * <code>Infinity</code>.
 * <p>
 * 
 * <li><b>-m option</b> &ndash; The "<code>-m</code>" option outputs the main effect means. This is
 * useful to ensure that the data are properly extracted from the data matrix in computing the
 * <i>F</i> statistics for the various main effects and interaction effects. For example, if the
 * data are improperly nested for two-factor designs, this error will be apparent by comparing the
 * output from this option against a manual calculation of the effect means.
 * <p>
 * 
 * <li><b>-h option</b> &ndash; The "<code>-h</code>" option is used if the data file contains
 * header lines. In this case, the data file must have four header lines preceding the data,
 * formatted as follows:
 * <p>
 * 
 * <pre>
     DV: &lt;dependent_variable_name&gt;
     F1: &lt;f1_name&gt;, &lt;f1_level_1_name&gt;, &lt;f1_level_2_name&gt;, ...
     F2: &lt;f2_name&gt;, &lt;f2_level_1_name&gt;, &lt;f2_level_2_name&gt;, ...
     F3: &lt;f3_name&gt; 
 </pre>
 * 
 * Note: The names for the levels of F3 are obtained from the last column in the raw data file.
 * <p>
 * 
 * The header option is strictly cosmetic. The output ANOVA table (<code>-a</code> option) and main
 * effect means ( <code>-m</code> option) will identify the dependent variable and the names and
 * levels of the factors, as given.
 * <p>
 * </ul>
 * 
 * An example for each supported experiment design follows. For comparison, each analysis is also
 * shown using a commercially available statistics package called <i>StatView</i> (currently
 * available as <i>JMP</i>; http://www.jmp.com/). The named data files are contained in the zip file
 * with the <code>Anova2</code> application and API.
 * <p>
 * 
 * <b>ONE-WAY WITH ONE WITHIN-SUBJECTS FACTOR</b>
 * <p>
 * 
 * The file <a href="dix-example-10x2.txt"><code>dix-example-10x2.txt</code></a> contains
 * <p>
 * 
 * <pre>
     656,702
     259,339
     612,658
     609,645
     1049,1129
     1135,1179
     542,604
     495,551
     905,893
     715,803
 </pre>
 * 
 * The data are hypothetical and appear in an example in Dix et al.'s <i>Human-Computer
 * Interaction</i> (Prentice Hall, 2004, 3rd ed., p. 337). The single factor (F1) is Icon Design
 * with two levels: Natural and Abstract. The data entries are the measurements on the dependent
 * variable Task Completion Time (seconds). The data in the first column are the task completion
 * time measurements for the Natural icons, while the data in the second column are the measurements
 * for the Abstract icons. Each row contains the measurements taken on one participant. The
 * hypothetical experiment used 10 participants.
 * <p>
 * 
 * The mean task completion times (not shown) are 697.7 s for the Natural icons and 750.3 s for the
 * Abstract icons. An analysis of variance determines if there is a statistically significant
 * difference between these means or if the difference is likely due to chance. The analysis is
 * performed as follows:
 * <p>
 * 
 * <blockquote><a href="Anova2-7.jpg"><img src="Anova2-7.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * 
 * As seen in the table, and as might appear in a research paper, "The experiment revealed a
 * significant effect of Icon Type on Task Completion Time (<i>F</i><sub>1,9</sub> = 33.36, <i>p</i>
 * < .0005)." Even though <i>p</i> = .0003 in the ANOVA table, it is typically reported in research
 * papers as <i>p</i> < <i>n</i>, where <i>n</i> is the closest more conservative value from the set
 * .05, .01, .005, .001, .0005, .0001. Note also that in North American publications, the zero
 * preceding the decimal point is typically omitted (because <i>p</i> is constrained between 0 and
 * 1).
 * <p>
 * 
 * Two other outcomes are worth noting, where the results are non-significant. If <i>p</i> is above
 * .05 and <i>F</i> > 1, <i>p</i> is reported as "<i>p</i> > .05". This means there is a greater
 * than 5% chance that the differences in the means is due to chance. This is sufficient lack of
 * confidence to deem the difference in the means "not significant". If <i>p</i> is above .05 and
 * <i>F</i> &le; 1, then <i>p</i> is not reported at all, but is replaced with "ns" meaning
 * "not significant". This format is used is because it is impossible for differences in the means
 * to be significant where <i>F</i> &le; 1.
 * <p>
 * 
 * The results above are shown below in an ANOVA on the same data using <i>StatView</i>.
 * <p>
 * 
 * <blockquote> <img src="Anova2-1.gif"> </blockquote>
 * <p>
 * 
 * <i>Lambda</i> and <i>Power</i> are not calculated in <code>Anova2</code>. <i>Lambda</i> is a
 * measure of the noncentrality of the <i>F</i> distribution, calculated as <i>F</i> &times;
 * <i>N</i>, where <i>N</i> is the degrees of freedom of the effect. <i>Power</i>, which ranges from
 * 0 to 1, is the ability to detect an effect, if there is one. The closer to one, the more the
 * experiment is likely to find an effect, if one exists in the population. <i>Power</i> > .80 is
 * generally considered acceptable; i.e., if <i>p</i> is significant and <i>Power</i> > .80, then it
 * is likely that the effect found actually exists.
 * <p>
 * 
 * <b>TWO-WAY WITH ONE WITHIN-SUBJECTS FACTOR AND ONE BETWEEN SUBJECTS FACTOR</b>
 * <p>
 * 
 * The hypothetical experiment described by Dix et al. was a within-subjects design and would likely
 * use counterbalancing to cancel the learning effects that might occur as participants advanced
 * from the first test condition to the second. With two conditions, the participants are divided
 * into two groups of equal size. Half the participants would be tested on the Natural icons first
 * followed by the Abstract icons, while the other half would be tested in the reverse order. Like
 * this, "Group" is a between-subjects factor with five participants in each group. To include this
 * in the analysis, we append a column to the data file, creating a new data file called <a
 * href="dix-example-h10x2b.txt"><code>dix-example-h10x2b.txt</code></a>. The new column identifies
 * the groups as either "NA" (Natural first, Abstract second) or "AN" (Abstract first, Natural
 * second). The file is also modified to include header lines, as per the requirements of the
 * <code>-h</code> option (see above). Here are the data:
 * <p>
 * 
 * <pre>
     DV: Completion Time (s)
     F1: Icon Type, Natural, Abstract
     F2: .
     F3: Group
     656,702,NA
     259,339,NA
     612,658,NA
     609,645,NA
     1049,1129,NA
     1135,1179,AN
     542,604,AN
     495,551,AN
     905,893,AN
     715,803,AN
 </pre>
 * 
 * Note that the data for each group are in consecutive rows. This is not a requirement, however, as
 * the data are sorted by the last column before performing the ANOVA. (Note: The data for
 * participant #1 is the data in the first row of sorted data. This point is only relevant with
 * <code>-m</code>, which outputs the participant means.)
 * <P>
 * 
 * To analyse these data, we replace "." with "2" for <code>f3</code>, indicating the presence of a
 * between-subjects factor with 2 groups. The <code>-h</code> option is also needed:
 * <p>
 * 
 * <blockquote><a href="Anova2-8.jpg"><img src="Anova2-8.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * The Group effect was not statistically significant (<i>F</i><sub>1,8</sub> = 0.466, ns). This is
 * good news, since it means counterbalancing worked; i.e., any learning effect that might have
 * occurred for the AN group was effectively offset by a similar and opposing learning effect for
 * the NA group. The Icon Type (F1) &times; Group interaction effect also failed to achieve
 * statistical significance (<i>F</i><sub>1,8</sub> = 0.277, ns). This means there was no
 * asymmetrical transfer of skill, also good news. Asymmetric transfer of skill means some aspect of
 * one condition helped (or hindered!) the other condition, without a corresponding reverse effect.
 * <p>
 * 
 * The same analysis in <i>StatView</i> appears as
 * <p>
 * 
 * <blockquote> <img src="Anova2-2.gif"> </blockquote>
 * <p>
 * 
 * <b>TWO-WAY WITH TWO WITHIN-SUBJECTS FACTORS</b>
 * <p>
 * 
 * For the next example, we use data from an experiment on "eye typing" -- the use of eye tracking
 * technology for text entry using an on-screen soft keyboard. The experimental methodology and data
 * analyses are described in "<a href="http://www.yorku.ca/mack/chi03d.html
 * ">Audio and visual feedback during eye typing </a>" (Majaranta, MacKenzie, Aula, & Räihä, <i>CHI
 * 2003</i>). The experiment was a 4 &times; 4 repeated-measures design with 13 participants. The
 * factors and levels were as follows:
 * <p>
 * 
 * <blockquote> <img src="Anova2-2b.gif"> </blockquote>
 * <p>
 * 
 * There was no counterbalancing as the order of presenting the feedback modes was randomized.
 * <p>
 * 
 * Although there were several dependent variables, only error rate (%) is presented here. The error
 * rate ANOVA table is in <a href="errorrate-h13x16.txt"><code>errorrate-h13x16.txt</code></a>:
 * (abbreviated to fit page)
 * <p>
 * 
 * <pre>

     DV: Error Rate (%)
     F1: Feedback, Speech Only, Click+Visual, Speech+Visual, Visual Only
     F2: Block, B1, B2, B3, B4
     F3: .
     0.0,0.0,0.0,0.0,0.0,0.7142857142000001,0.0,0.0,0.0,0.0,0.0,0.0, ...
     0.0,0.0,0.0,0.0,1.7391304347999998,0.0,0.0,0.0,0.0,0.909090909, ...
     0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,4.4999282534,3.636363636,0.0,0.0, ...
     0.0,0.0,0.0,0.0,1.9047619048000002,0.0,0.0,1.8253968254,0.0, ...
     0.0,0.0,0.0,0.0,1.1111111112,1.1111111112,0.0,0.0,0.0,0.0,0.0, ...
     0.0,0.0,0.0,0.0,1.052631579,0.0,0.0,2.222222222,0.0,0.0, ...
     0.909090909,0.0,0.0,0.0,0.7407407408,1.7391304347999998, ...
     0.0,0.0,0.0,0.0,0.909090909,0.0,0.0,0.0,0.0,0.0,0.0, ...
     0.0,0.0,0.0,0.8695652173999999,1.4814814814,0.0,0.0,0.0, ...
     0.0,0.0,0.0,0.0,0.7407407408,0.0,0.0,1.8461538462,1.052631579, ...
     0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.7407407408,0.0,0.0, ...
     1.6103059582,0.0,0.0,0.0,2.0,0.0,1.052631579,0.7407407408,0.0, ...
     4.350649350199999,0.0,1.052631579,0.0,1.3793103448,3.052631579, ...
 </pre>
 * 
 * The data entries are full precision, so the table above is unsightly. The four header lines were
 * manually inserted to improve the output generated by <code>Anova2</code>.
 * <p>
 * 
 * The <code>-m</code> option may be used prior to the analysis, to view the overall effect means:
 * <p>
 * 
 * <blockquote><a href="Anova2-9.jpg"><img src="Anova2-9.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * Use of the <code>-m</code> option is highly recommended in situations were more than one factor
 * is present. The means for the levels of the factors should be compared against the same values
 * computed manually or in a spreadsheet to ensure the nesting of data is correct.
 * <p>
 * 
 * The analysis of variance is performed as follows:
 * <p>
 * 
 * <blockquote><a href="Anova2-10.jpg"><img src="Anova2-10.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * As seen in the table, the main effect of Feedback Mode on Error Rate was significant
 * (<i>F</i><sub>3,36</sub> = 4.92, <i>p</i> < .01). There was also a significant improvement in
 * entry speed with practice as evident by the significant effect of Block (<i>F</i><sub>3,36</sub>
 * = 4.98, <i>p</i> < .001). However, the Feedback Mode &times; Block interaction effect was not
 * significant (<i>F</i><sub>9,108</sub> = 1.73, <i>p</i> > .05).
 * <p>
 * 
 * The same data similarly analysed in <i>StatView</i> yield the following ANOVA table:
 * <p>
 * 
 * <blockquote> <img src="Anova2-3.gif"> </blockquote>
 * <p>
 * 
 * <b>THREE-WAY WITH TWO WITHIN-SUBJECTS FACTORS AND ONE BETWEEN-SUBJECTS FACTOR</b>
 * 
 * <p>
 * 
 * The file <a href="softkeyboard-h12x10b.txt"><code>softkeyboard-h12x10b.txt</code></a> contains
 * the data from an experiment comparing two layouts of soft keyboards. The experiment used 12
 * participants in a 2 &times; 5 repeated-measures design. The participants tapped the phrase
 * "the quick brown fox jumps over the lazy dog" five times on each of two soft keyboard layouts.
 * Each entry of a phrase is called a "trial". The dependent variable was Entry Speed in words per
 * minute. There were two independent variables, or factors:
 * <p>
 * 
 * <blockquote> <img src="Anova2-3b.gif"> </blockquote>
 * <p>
 * 
 * Testing was counterbalanced: Each participant entered the phrase five times with one layout, then
 * five times with the other layout. Half the participants used Opti first, following by Qwerty. The
 * other half used the layouts in the reverse order. Thus, Group was a between-subjects factor with
 * two levels, Group A and Group B.
 * <p>
 * 
 * The data file was edited to show the variable names in header lines and the participant group as
 * the last entry on each data line: (abbreviated to fit page)
 * <p>
 * 
 * <pre>
     DV: Entry Speed (wpm)
     F1: Layout, Opti, Qwerty
     F2: Trial, T1, T2, T3, T4, T5
     F3: Group
     7.589351375,12.37706884, ... 31.34872418,31.55963303,33.81389253,A
     9.32417781,12.900000000, ... 28.89137738,29.94776553,35.0305499,A
     9.207708779,9.504512802, ... 24.14599906,27.95232936,28.10457516,A
     7.158712542,7.754733995, ... 25.0242483,20.65652522,24.80769231,A
     9.532606688,13.18007663, ... 28.58725762,30.75089392,30.55062167,A
     9.290601368,11.66628985, ... 26.9733403,24.71264368,28.74651811,A
     9.417776967,9.194583036, ... 27.21518987,28.04347826,28.32052689,B
     5.347150259,7.188631931, ... 20.31496063,19.28971963,20.39525692,B
     14.1797197,15.10980966,  ... 30.71428571,31.67587477,34.01450231,B
     8.970792768,10.39693734, ... 27.68240343,25.51928783,25.12171373,B
     9.552017771,12.69372694, ... 30.97238896,32.33082707,33.7254902,B
     8.510638298,12.11267606, ... 33.07692308,32.84532145,32.04968944,B
 </pre>
 * 
 * The main effect means are computed as follows:
 * <p>
 * 
 * <blockquote><a href="Anova2-11.jpg"><img src="Anova2-11.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * Entry speed in words per minute was much faster with the Qwerty layout (26.5 wpm) than with the
 * Opti layout (12.3 wpm). Let's see if the variances were sufficiently low to deem the difference
 * in the means statistically significant:
 * <p>
 * 
 * <blockquote><a href="Anova2-12.jpg"><img src="Anova2-12.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * Yes. The <i>F</i> statistic, which is the ratio of the mean squares (6062.238 / 7.606 = 797.0),
 * is extremely high. Not surprisingly, the <i>F</i> statistic for the main effect of Layout on
 * Entry Speed is highly significant (<i>F</i><sub>1,10</sub> = 797.0, <i>p</i> < .0001). In all,
 * the table shows three main effects and four interaction effects. There is considerable leeway in
 * presenting the results in a research paper. See Experiment 1 in
 * "<a href="http://www.yorku.ca/mack
 * /cascon2007.html">Using paper mockups for evaluating soft keyboard layouts</a>" (MacKenzie &
 * Read, <i>CASCON 2007</i>) for an example of how the results above might be reported.
 * <p>
 * 
 * The results above are confirmed using <i>StatView</i>:
 * <p>
 * 
 * <blockquote> <img src="Anova2-4.gif"> </blockquote>
 * <p>
 * 
 * <b>ONE-WAY WITH ONE BETWEEN-SUBJECTS FACTOR</b>
 * <p>
 * 
 * A one-way between-subjects design might be used, for example, to test whether an interface or
 * interaction technique works better with left-handed vs. right-handed users (or with males vs.
 * females). In this case, the design must be between-subjects because a participant cannot be both
 * left-handed and right-handed (or male and female!). Two groups of participants are required.
 * Let's consider the case where five left-handed users (L) and five right-handed users (R) are
 * measured on a task. The independent variable is Handedness with two levels, Left and Right, and
 * the dependent variable is Time (seconds) to complete a task. Here are the example data, stored in
 * <a href="anova-h10b.txt"> <code>anova-h10b.txt</code></a>:
 * <p>
 * 
 * <pre>
     DV: Time (s)
     F1: .
     F2: .
     F3: Handedness
     25.6,L
     23.4,L
     19.4,L
     28.1,L
     25.9,L
     14.3,R
     22.0,R
     30.4,R
     21.1,R
     19.3,R
 </pre>
 * 
 * The means (not shown) for the Left- and Right-handed groups were 28.48 s and 21.42 s,
 * respectively. So, the Left-handed group took, on average, 33% longer to complete the task. That's
 * a huge performance difference, but is the difference in the means statistically significant?
 * Let's see. The analysis is performed as follows:
 * <p>
 * 
 * <blockquote><a href="Anova2-13.jpg"><img src="Anova2-13.jpg" width="600"></a></blockquote>
 * <p>
 * 
 * Despite the observation that the Left-handed group took considerable longer to complete the task,
 * the difference between the groups was not statistically significant (<i>F</i><sub>1,8</sub> =
 * 1.04, <i>p</i> > .05). This might be partly attributed to the small number of participants
 * tested. It might also be attributed simply to a lack of bias in the interface for Left-handed vs.
 * Right-handed users.
 * <p>
 * 
 * Using <i>StatView</i>, the above results are confirmed:
 * <p>
 * 
 * <blockquote> <img src="Anova2-5.gif"> </blockquote>
 * <p>
 * 
 * <b>A note on the calculations:</b><br>
 * 
 * The trickiest part is the calculation of <i>p</i>, representing the significance of <i>F</i>.
 * This comes by way of the method <code>FProbability</code> in the <code>Statistics</code> class in
 * the University of Waikato's <code>weka.core</code> package. This package was obtained from
 * <p>
 * 
 * <center> <a href="http://www.cs.waikato.ac.nz/ml/weka/index.html">
 * http://www.cs.waikato.ac.nz/ml/weka/index.html</a> </center>
 * <p>
 * 
 * <code>Statistics.class</code> copyright notice:
 * <P>
 * <blockquote>
 * -----<br>
 * Class implementing some distributions, tests, etc. The code is mostly adapted from the CERN Jet
 * Java libraries: Copyright 2001 University of Waikato Copyright 1999 CERN - European Organization
 * for Nuclear Research. Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that the above copyright
 * notice appear in all copies and that both that copyright notice and this permission notice appear
 * in supporting documentation. CERN and the University of Waikato make no representations about the
 * suitability of this software for any purpose. It is provided "as is" without expressed or implied
 * warranty.<br>
 * -----<br>
 * </blockquote>
 * <p>
 * 
 * 
 * @author Scott MacKenzie 2003-2014
 */

public class Anova2
{

	public Anova2()
	{
	}

	public static void main(String args[]) throws IOException
	{
		if (args.length < 5 || args.length > 9)
			usage();

		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException filenotfoundexception)
		{
			System.out.println("File not found: " + args[0]);
			System.exit(0);
		}

		int numberOfParticipants = -1;
		int levelsOfF1 = -1;
		int levelsOfF2 = -1;
		int levelsOfF3 = -1;

		// ------------------------------------
		// get parameters for experiment design
		// ------------------------------------

		try
		{
			numberOfParticipants = Integer.parseInt(args[1]);
			levelsOfF1 = args[2].equals(".") ? 1 : Integer.parseInt(args[2]); // within-subjects
			levelsOfF2 = args[3].equals(".") ? 1 : Integer.parseInt(args[3]); // within-subjects
			levelsOfF3 = args[4].equals(".") ? 1 : Integer.parseInt(args[4]); // between-subjects
		} catch (NumberFormatException nfe)
		{
			System.out.println("Number format exception.  Check arguments!");
			usage();
		}

		if (numberOfParticipants % levelsOfF3 != 0)
		{
			System.out.println("Command-line error: f3 must divide evenly into p!");
			usage();
		}

		// ---------
		// set flags
		// ---------

		boolean validF1 = levelsOfF1 > 1;
		boolean validF2 = levelsOfF2 > 1;
		boolean validF3 = levelsOfF3 > 1;
		boolean outputAnovaTable = false;
		boolean outputDebugInfo = false;
		boolean outputMainEffectMeans = false;
		boolean headerOption = false;
		for (int i = 5; i < args.length; i++)
		{
			if (args[i].equals("-a"))
				outputAnovaTable = true;
			if (args[i].equals("-d"))
				outputDebugInfo = true;
			if (args[i].equals("-h"))
				headerOption = true;
			if (args[i].equals("-m"))
				outputMainEffectMeans = true;
		}

		// must have at least one output option
		if (!outputAnovaTable && !outputDebugInfo && !outputMainEffectMeans)
			usage();

		// -------------------------------------------------------------
		// set default names for dependent variable, factors, and levels
		// -------------------------------------------------------------

		String dvName = "DV";
		String f1Name = "F1";
		String f2Name = "F2";
		String f3Name = "F3";

		String levelNamesForF1[] = new String[levelsOfF1];
		String levelNamesForF2[] = new String[levelsOfF2];
		String levelNamesForF3[] = new String[levelsOfF3];
		for (int i = 0; i < levelsOfF1; i++)
			levelNamesForF1[i] = "L" + (i + 1);

		for (int i = 0; i < levelsOfF2; i++)
			levelNamesForF2[i] = "L" + (i + 1);

		// Note: names for levels of F3 obtained from last column in data file

		// ------------------------------------------------------------------
		// if -h option, get names of dependent variable, factors, and levels
		// ------------------------------------------------------------------

		if (headerOption)
		{
			// dependent variable
			String s = br.readLine();
			StringTokenizer st = new StringTokenizer(s, ":,");
			if (st.countTokens() != 2)
				headerError();
			String s2 = st.nextToken();
			if (!s2.equals("DV"))
				headerError();
			dvName = st.nextToken().trim();

			// 1st independent variable (within-subjects)
			s = br.readLine();
			st = new StringTokenizer(s, ":,");
			int count = st.countTokens();
			if (count != 2 && count != 2 + levelsOfF1)
				headerError();
			st.nextToken();
			f1Name = st.nextToken().trim();
			for (int i = 0; i < levelsOfF1; i++)
				levelNamesForF1[i] = count <= 2 ? "L" + (i + 1) : st.nextToken().trim();

			// 2nd independent variable (within-subjects)
			s = br.readLine();
			st = new StringTokenizer(s, ":,");
			count = st.countTokens();
			if (count != 2 && count != 2 + levelsOfF2)
				headerError();
			st.nextToken();
			f2Name = st.nextToken().trim();
			for (int i = 0; i < levelsOfF2; i++)
				levelNamesForF2[i] = count <= 2 ? "L" + (i + 1) : st.nextToken().trim();

			// 3rd independent variable (between-subjects)
			s = br.readLine();
			st = new StringTokenizer(s, ":,");
			count = st.countTokens();
			if (count != 2)
				headerError();
			st.nextToken();
			f3Name = st.nextToken().trim();
			// Note: names for levels of F3 obtained from last column in data file
		}

		// ---------
		// load data
		// ---------

		// determine the number of columns
		int numberOfRawDataColumns = levelsOfF1 * levelsOfF2;
		if (validF3)
			++numberOfRawDataColumns;

		// load the raw raw data into a matrix (as Strings)
		String rawData[][] = new String[numberOfParticipants][numberOfRawDataColumns];
		String s;
		int rowCount = 0;
		while ((s = br.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(s, "\n\t ,");

			// verify the number of columns (before attempting to read)
			if (st.countTokens() != numberOfRawDataColumns)
			{
				System.out.printf("Wrong number of columns at line %d: %d (%d required)\n", rowCount + 1, st
						.countTokens(), numberOfRawDataColumns);
				System.exit(0);
			}

			// make sure we haven't got too much data (check now to avoid exception)
			if (rowCount == numberOfParticipants)
			{
				System.out.printf("Too many rows of data! (%d expected)\n", numberOfParticipants);
				System.exit(0);
			}

			// read the raw data
			for (int i = 0; i < numberOfRawDataColumns; ++i)
				rawData[rowCount][i] = st.nextToken();

			// Note: number of rows = number of participants
			++rowCount;
		}

		// verify that the correct number of rows (participants) were read
		if (rowCount != numberOfParticipants)
		{
			System.out.printf("Not enough data! (%d rows expected)\n", numberOfParticipants);
			System.exit(0);
		}

		// copy the matrix into new matrix (which will be sorted if there is an F3 factor)
		String[][] rawDataSortedByF3 = new String[numberOfParticipants][numberOfRawDataColumns];
		for (int i = 0; i < rawDataSortedByF3.length; ++i)
			for (int j = 0; j < rawDataSortedByF3[i].length; ++j)
				rawDataSortedByF3[i][j] = rawData[i][j];

		// if there is a between-subjects factor, sort the matrix on the last column
		if (validF3)
		{
			Arrays.sort(rawDataSortedByF3, new ByLastColumn());

			// retrieve the names of the F3 identifiers (from 1st Participant in each group)
			for (int i = 0; i < levelsOfF3; ++i)
				levelNamesForF3[i] = rawDataSortedByF3[i * (numberOfParticipants / levelsOfF3)][numberOfRawDataColumns - 1];

			// verify the correctness of the between-subjects identifiers in the last column
			for (int i = 0; i < numberOfParticipants; ++i)
			{
				String identifierExpected = levelNamesForF3[i / (numberOfParticipants / levelsOfF3)];
				String identifierFound = rawDataSortedByF3[i][numberOfRawDataColumns - 1];
				if (!identifierExpected.equals(identifierFound))
				{
					System.out.printf("Wrong F3 identifier in sorted data at line %d: %s (%s expected)\n", i + 1,
							identifierFound, identifierExpected);
					System.exit(0);
				}
			}
		}

		// transfer raw data matrix (String objects) into anova data matrix (doubles)
		int numberOfDataColumns = levelsOfF1 * levelsOfF2;
		double anovaData[][] = new double[numberOfParticipants][numberOfDataColumns];
		for (int i = 0; i < rawDataSortedByF3.length; ++i)
			for (int j = 0; j < numberOfDataColumns; ++j)
				anovaData[i][j] = Double.parseDouble(rawDataSortedByF3[i][j]);

		// -------------
		// compute means
		// -------------

		// grand mean
		double grandMean = 0.0D;
		for (int i = 0; i < numberOfParticipants; i++)
		{
			for (int j = 0; j < numberOfDataColumns; j++)
				grandMean += anovaData[i][j];
		}
		grandMean /= numberOfParticipants * numberOfDataColumns;

		// Participant means
		String participantNames[] = new String[numberOfParticipants];
		double participantMeans[] = new double[numberOfParticipants];
		for (int i = 0; i < numberOfParticipants; i++)
		{
			for (int j = 0; j < numberOfDataColumns; j++)
			{
				participantMeans[i] += anovaData[i][j];
				participantNames[i] = "p" + (i + 1);
			}
			participantMeans[i] /= numberOfDataColumns;
		}

		// F1 means
		double levelMeansForF1[] = new double[levelsOfF1];
		for (int i = 0; i < levelsOfF1; i++)
		{
			for (int j = 0; j < levelsOfF2; j++)
			{
				for (int k = 0; k < numberOfParticipants; k++)
					levelMeansForF1[i] += anovaData[k][i * levelsOfF2 + j];
			}
			levelMeansForF1[i] /= numberOfParticipants * levelsOfF2;
		}

		// F2 means
		double levelMeansForF2[] = new double[levelsOfF2];
		for (int i = 0; i < levelsOfF2; i++)
		{
			for (int j = 0; j < levelsOfF1; j++)
			{
				for (int k = 0; k < numberOfParticipants; k++)
					levelMeansForF2[i] += anovaData[k][i + levelsOfF2 * j];
			}
			levelMeansForF2[i] /= numberOfParticipants * levelsOfF1;
		}

		// F1 x F2 means (Note: same as F1 means if there is no F2)
		double levelMeansForF1xF2[] = new double[numberOfDataColumns];
		String levelNamesForF1xF2[] = new String[numberOfDataColumns];
		for (int i = 0; i < numberOfDataColumns; i++)
		{
			for (int j = 0; j < numberOfParticipants; j++)
			{
				levelMeansForF1xF2[i] += anovaData[j][i];
				if (levelsOfF1 > 1 && levelsOfF2 > 1)
					levelNamesForF1xF2[i] = levelNamesForF1[j % levelsOfF1] + "," + levelNamesForF2[i % levelsOfF2];
				else
					levelNamesForF1xF2[i] = ".";
			}
			levelMeansForF1xF2[i] /= numberOfParticipants;
		}

		// F1 x Participant means
		double levelMeansForF1xP[] = new double[levelsOfF1 * numberOfParticipants];
		String levelNamesForF1xP[] = new String[levelsOfF1 * numberOfParticipants];
		for (int i = 0; i < levelsOfF1; i++)
		{
			for (int j = 0; j < numberOfParticipants; j++)
			{
				for (int k = 0; k < levelsOfF2; k++)
					levelMeansForF1xP[j + i * numberOfParticipants] += anovaData[j][k + i * levelsOfF2];

				levelNamesForF1xP[j + i * numberOfParticipants] = levelNamesForF1[i] + "," + participantNames[j];
				levelMeansForF1xP[j + i * numberOfParticipants] /= levelsOfF2;
			}
		}

		// F2 x Participant means
		double levelMeansForF2xP[] = new double[levelsOfF2 * numberOfParticipants];
		String levelNamesForF2xP[] = new String[levelsOfF2 * numberOfParticipants];
		for (int i = 0; i < levelsOfF2; i++)
		{
			for (int j = 0; j < numberOfParticipants; j++)
			{
				for (int k = 0; k < levelsOfF1; k++)
					levelMeansForF2xP[j + i * numberOfParticipants] += anovaData[j][i + k * levelsOfF2];

				levelNamesForF2xP[j + i * numberOfParticipants] = levelNamesForF2[i] + "," + participantNames[j];
				levelMeansForF2xP[j + i * numberOfParticipants] /= levelsOfF1;
			}
		}

		// F3 means
		double levelMeansForF3[] = new double[levelsOfF3];
		for (int i = 0; i < levelsOfF3; i++)
		{
			for (int j = 0; j < numberOfParticipants / levelsOfF3; j++)
			{
				for (int k = 0; k < numberOfDataColumns; k++)
					levelMeansForF3[i] += anovaData[i * (numberOfParticipants / levelsOfF3) + j][k];
			}
			levelMeansForF3[i] /= (numberOfParticipants / levelsOfF3) * numberOfDataColumns;
		}

		// F1 x F3 means
		double levelMeansForF1xF3[] = new double[levelsOfF1 * levelsOfF3];
		String levelNamesForF1xF3[] = new String[levelsOfF1 * levelsOfF3];
		for (int i = 0; i < levelsOfF1 * levelsOfF3; i++)
		{
			for (int j = 0; j < numberOfParticipants / levelsOfF3; j++)
			{
				for (int k = 0; k < levelsOfF2; k++)
				{
					int rowIdx = (i / levelsOfF1) * (numberOfParticipants / levelsOfF3) + j;
					int colIdx = (i % levelsOfF1) * levelsOfF2 + k;
					levelMeansForF1xF3[i] += anovaData[rowIdx][colIdx];
				}
			}
			levelMeansForF1xF3[i] /= levelsOfF2 * (numberOfParticipants / levelsOfF3);
			levelNamesForF1xF3[i] = ".";
		}

		// F2 x F3 means
		double levelMeansForF2xF3[] = new double[levelsOfF2 * levelsOfF3];
		String levelNamesForF2xF3[] = new String[levelsOfF2 * levelsOfF3];
		for (int i = 0; i < levelsOfF2 * levelsOfF3; i++)
		{
			for (int j = 0; j < numberOfParticipants / levelsOfF3; j++)
			{
				for (int k = 0; k < levelsOfF1; k++)
				{
					int rowIdx = (i / levelsOfF2) * (numberOfParticipants / levelsOfF3) + j;
					int colIdx = i % levelsOfF2 + k * levelsOfF2;
					levelMeansForF2xF3[i] += anovaData[rowIdx][colIdx];
				}
			}
			levelMeansForF2xF3[i] /= levelsOfF1 * (numberOfParticipants / levelsOfF3);
			levelNamesForF2xF3[i] = ".";
		}

		// F1 x F2 x F3 means
		double levelMeansForF1xF2xF3[] = new double[levelsOfF1 * levelsOfF2 * levelsOfF3];
		String levelNamesForF1xF2xF3[] = new String[levelsOfF1 * levelsOfF2 * levelsOfF3];
		for (int i = 0; i < levelsOfF1 * levelsOfF2 * levelsOfF3; i++)
		{
			for (int j = 0; j < numberOfParticipants / levelsOfF3; j++)
			{
				int rowIdx = (i / (levelsOfF1 * levelsOfF2)) * (numberOfParticipants / levelsOfF3) + j;
				int colIdx = i % (levelsOfF1 * levelsOfF2);
				levelMeansForF1xF2xF3[i] += anovaData[rowIdx][colIdx];
			}
			levelMeansForF1xF2xF3[i] /= numberOfParticipants / levelsOfF3;
			levelNamesForF1xF2xF3[i] = ".";
		}

		// -----------------------
		// compute sums of squares
		// -----------------------

		double ssTotal = 0.0D;
		for (int i = 0; i < numberOfParticipants; i++)
		{
			for (int j = 0; j < numberOfDataColumns; j++)
				ssTotal += (anovaData[i][j] - grandMean) * (anovaData[i][j] - grandMean);
		}

		double ssParticipants = 0.0D;
		for (int i = 0; i < numberOfParticipants; i++)
			ssParticipants += (participantMeans[i] - grandMean) * (participantMeans[i] - grandMean);
		ssParticipants *= numberOfDataColumns;

		double ssF1 = 0.0D;
		for (int i = 0; i < levelsOfF1; i++)
			ssF1 += (levelMeansForF1[i] - grandMean) * (levelMeansForF1[i] - grandMean);
		ssF1 *= (numberOfParticipants * numberOfDataColumns) / levelsOfF1;

		double ssF2 = 0.0D;
		for (int i = 0; i < levelsOfF2; i++)
			ssF2 += (levelMeansForF2[i] - grandMean) * (levelMeansForF2[i] - grandMean);
		ssF2 *= levelsOfF1 * numberOfParticipants;

		double ssF1xF2 = 0.0D;
		for (int i = 0; i < numberOfDataColumns; i++)
			ssF1xF2 += (levelMeansForF1xF2[i] - grandMean) * (levelMeansForF1xF2[i] - grandMean);
		ssF1xF2 = ssF1xF2 * (double)numberOfParticipants - ssF1 - ssF2;

		double ssF1xP = 0.0D;
		for (int i = 0; i < levelsOfF1 * numberOfParticipants; i++)
			ssF1xP += (levelMeansForF1xP[i] - grandMean) * (levelMeansForF1xP[i] - grandMean);
		ssF1xP = ssF1xP * (double)levelsOfF2 - ssF1 - ssParticipants;

		double ssF2xP = 0.0D;
		for (int i = 0; i < levelsOfF2 * numberOfParticipants; i++)
			ssF2xP += (levelMeansForF2xP[i] - grandMean) * (levelMeansForF2xP[i] - grandMean);
		ssF2xP = ssF2xP * (double)levelsOfF1 - ssF2 - ssParticipants;

		double ssF1xF2xP = ssTotal - ssParticipants - ssF1 - ssF2 - ssF1xF2 - ssF1xP - ssF2xP;

		double ssF3 = 0.0D;
		for (int i = 0; i < levelMeansForF3.length; i++)
			ssF3 += (levelMeansForF3[i] - grandMean) * (levelMeansForF3[i] - grandMean);
		ssF3 *= (numberOfParticipants * numberOfDataColumns) / levelMeansForF3.length;

		double ssPxF3 = ssParticipants - ssF3;

		double ssF1xF3 = 0.0D;
		for (int i = 0; i < levelMeansForF1xF3.length; i++)
			ssF1xF3 += Math.pow(levelMeansForF1xF3[i] - grandMean, 2D);
		ssF1xF3 *= (numberOfParticipants * numberOfDataColumns) / levelMeansForF1xF3.length;
		ssF1xF3 -= ssF1 + ssF3;

		double ssF1xPxF3 = ssF1xP - ssF1xF3;
		double ssF2xF3 = 0.0D;
		for (int i = 0; i < levelMeansForF2xF3.length; i++)
			ssF2xF3 += Math.pow(levelMeansForF2xF3[i] - grandMean, 2D);

		ssF2xF3 *= (numberOfParticipants * numberOfDataColumns) / levelMeansForF2xF3.length;
		ssF2xF3 -= ssF2 + ssF3;
		double ssF2xPxF3 = ssF2xP - ssF2xF3;
		double ssF1xF2xF3 = 0.0D;
		for (int i = 0; i < levelMeansForF1xF2xF3.length; i++)
			ssF1xF2xF3 += Math.pow(levelMeansForF1xF2xF3[i] - grandMean, 2D);

		ssF1xF2xF3 *= (numberOfParticipants * numberOfDataColumns) / levelMeansForF1xF2xF3.length;
		ssF1xF2xF3 -= ssF1 + ssF2 + ssF3 + ssF1xF2 + ssF1xF3 + ssF2xF3;
		double ssF1xF2xPxF3 = ssF1xF2xP - ssF1xF2xF3;

		// --------------------------
		// compute degrees of freedom
		// --------------------------

		int dfP = numberOfParticipants - 1;
		int dfF1 = levelsOfF1 - 1;
		int dfF1xP = dfF1 * dfP;
		int dfF2 = levelsOfF2 - 1;
		int dfF2xP = dfF2 * dfP;
		int dfF1xF2 = dfF1 * dfF2;
		int dfF1xF2xP = dfF1 * dfF2 * dfP;
		int k16 = numberOfParticipants / levelsOfF3;
		int dfF3 = levelsOfF3 - 1;
		int dfF1xF3 = dfF1 * dfF3;
		int dfF1xPxF3 = dfF1 * levelsOfF3 * (k16 - 1);
		int dfF2xF3 = dfF2 * dfF3;
		int dfF2xPxF3 = dfF2 * levelsOfF3 * (k16 - 1);
		int dfPxF3 = levelsOfF3 * (k16 - 1);
		int dfF1xF2xF3 = dfF1 * dfF2 * dfF3;
		int dfF1xF2xPxF3 = dfF1 * dfF2 * levelsOfF3 * (k16 - 1);

		// --------------------
		// compute mean squares
		// --------------------

		double msParticipants = ssParticipants / (double)dfP;
		double msF1 = ssF1 / (double)dfF1;
		double msF1xP = ssF1xP / (double)dfF1xP;
		double msF1xPxF3 = ssF1xPxF3 / (double)dfF1xPxF3;
		double msF2 = ssF2 / (double)dfF2;
		double msF2xP = ssF2xP / (double)dfF2xP;
		double msF2xPxF3 = ssF2xPxF3 / (double)dfF2xPxF3;
		double msF1xF2 = ssF1xF2 / (double)dfF1xF2;
		double msF1xF2xP = ssF1xF2xP / (double)dfF1xF2xP;
		double msF3 = ssF3 / (double)dfF3;
		double msF1xF3 = ssF1xF3 / (double)dfF1xF3;
		double msF2xF3 = ssF2xF3 / (double)dfF2xF3;
		double msPxF3 = ssPxF3 / (double)dfPxF3;
		double msF1xF2xF3 = ssF1xF2xF3 / (double)dfF1xF2xF3;
		double msF1xF2xPxF3 = ssF1xF2xPxF3 / (double)dfF1xF2xPxF3;

		// --------------------
		// compute F statistics
		// --------------------

		double fF1 = validF3 ? msF1 / msF1xPxF3 : msF1 / msF1xP;
		double fF2 = validF3 ? msF2 / msF2xPxF3 : msF2 / msF2xP;
		double fF1xF2 = validF3 ? msF1xF2 / msF1xF2xPxF3 : msF1xF2 / msF1xF2xP;
		double fF3 = msF3 / msPxF3;
		double fF1xF3 = msF1xF3 / msF1xPxF3;
		double fF2xF3 = msF2xF3 / msF2xPxF3;
		double fF1xF2xF3 = msF1xF2xF3 / msF1xF2xPxF3;

		// ---------------------
		// compute probabilities
		// ---------------------

		double pF1 = 0.0D;
		double pF2 = 0.0D;
		double pF1xF2 = 0.0D;
		double pF3 = 0.0D;
		double pF1xF3 = 0.0D;
		double pF2xF3 = 0.0D;
		double pF1xF2xF3 = 0.0D;
		if (validF1)
			pF1 = Statistics.FProbability(fF1, dfF1, dfF1xP);
		if (validF2)
		{
			pF2 = Statistics.FProbability(fF2, dfF2, dfF2xP);
			pF1xF2 = validF3 ? Statistics.FProbability(fF1xF2, dfF1xF2, dfF1xF2xPxF3) : Statistics.FProbability(fF1xF2,
					dfF1xF2, dfF1xF2xP);
		}
		if (validF3)
		{
			pF3 = Statistics.FProbability(fF3, dfF3, dfPxF3);
			if (validF3 && validF1)
			{
				pF1xF3 = Statistics.FProbability(fF1xF3, dfF1xF3, dfF1xPxF3);
				if (validF3 && validF2)
				{
					pF2xF3 = Statistics.FProbability(fF2xF3, dfF2xF3, dfF2xPxF3);
					pF1xF2xF3 = Statistics.FProbability(fF1xF2xF3, dfF1xF2xF3, dfF1xF2xPxF3);
				}
			}
		}

		// -------------------------------------------------------------------------------------
		// The heaving lift is done! Start generating output, according to command-line options
		// -------------------------------------------------------------------------------------

		// -----------------------------
		// -d option (output debug info)
		// ------------------------------

		if (outputDebugInfo)
		{
			System.out.println("=============================");
			System.out.println("----- RAW DATA UNSORTED -----");
			System.out.println("============================");
			for (int i = 0; i < rawData.length; ++i)
			{
				for (int j = 0; j < rawData[i].length; ++j)
				{
					System.out.printf("%s", rawData[i][j]);
					if (j < rawData[i].length - 1)
						System.out.print(",");
				}
				System.out.println();
			}
			System.out.println();

			System.out.println("=================================");
			System.out.println("----- RAW DATA SORTED BY F3 -----");
			System.out.println("=================================");
			for (int i = 0; i < rawDataSortedByF3.length; ++i)
			{
				for (int j = 0; j < rawDataSortedByF3[i].length; ++j)
				{
					System.out.printf("%s", rawDataSortedByF3[i][j]);
					if (j < rawDataSortedByF3[i].length - 1)
						System.out.print(",");
				}
				System.out.println();
			}
			System.out.println();

			System.out.println("======================");
			System.out.println("----- ANOVA DATA -----");
			System.out.println("======================");
			for (int i = 0; i < anovaData.length; ++i)
			{
				for (int j = 0; j < anovaData[i].length; ++j)
				{
					System.out.printf("%s", anovaData[i][j]);
					if (j < anovaData[i].length - 1)
						System.out.print(",");
				}
				System.out.println();
			}
			System.out.println();

			System.out.println("=================");
			System.out.println("----- MEANS -----");
			System.out.println("=================");
			System.out.println("Grand mean: " + grandMean);
			printArray("m p", participantMeans, participantNames);
			printArray("m f1", levelMeansForF1, levelNamesForF1);
			printArray("m f2", levelMeansForF2, levelNamesForF2);
			printArray("m f2xp", levelMeansForF2xP, levelNamesForF2xP);
			printArray("m f1xp", levelMeansForF1xP, levelNamesForF1xP);
			printArray("m f1xf2", levelMeansForF1xF2, levelNamesForF1xF2);
			printArray("m f3", levelMeansForF3, levelNamesForF3);
			printArray("m f1xf3", levelMeansForF1xF3, levelNamesForF1xF3);
			printArray("m f2xf3", levelMeansForF2xF3, levelNamesForF2xF3);
			printArray("m f1xf2xf3", levelMeansForF1xF2xF3, levelNamesForF1xF2xF3);
			System.out.println("==============================");
			System.out.println("----- DEGREES OF FREEDOM -----");
			System.out.println("==============================");
			System.out.println("df p: " + dfP);
			System.out.println("df f1: " + dfF1);
			System.out.println("df f1xp: " + dfF1xP);
			System.out.println("df f1xp(f3): " + dfF1xPxF3);
			System.out.println("df f2: " + dfF2);
			System.out.println("df f2xp: " + dfF2xP);
			System.out.println("df f2xp(f3): " + dfF2xPxF3);
			System.out.println("df f1xf2: " + dfF1xF2);
			System.out.println("df f1xf2xp: " + dfF1xF2xP);
			System.out.println("df f3: " + dfF3);
			System.out.println("df p(f3): " + dfPxF3);
			System.out.println("df f1xf3: " + dfF1xF3);
			System.out.println("df f2xf3: " + dfF2xF3);
			System.out.println("df f1xf2xf3: " + dfF1xF2xF3);
			System.out.println("df f1xf2xp(f3): " + dfF1xF2xPxF3);
			System.out.println("===========================");
			System.out.println("----- SUMS OF SQUARES -----");
			System.out.println("===========================");
			System.out.println("ss total: " + ssTotal);
			System.out.println("ss p: " + ssParticipants);
			System.out.println("ss f1: " + ssF1);
			System.out.println("ss f1xp: " + ssF1xP);
			System.out.println("ss f2: " + ssF2);
			System.out.println("ss f2xp: " + ssF2xP);
			System.out.println("ss f1xf2: " + ssF1xF2);
			System.out.println("ss f1xf2xp: " + ssF1xF2xP);
			System.out.println("ss f3: " + ssF3);
			System.out.println("ss p(f3): " + ssPxF3);
			System.out.println("ss f1xf3: " + ssF1xF3);
			System.out.println("ss f1xp(f3): " + ssF1xPxF3);
			System.out.println("ss f2xf3: " + ssF2xF3);
			System.out.println("ss f2xp(f3): " + ssF2xPxF3);
			System.out.println("ss f1xf2xf3: " + ssF1xF2xF3);
			System.out.println("ss f1xf2xp(f3): " + ssF1xF2xPxF3);
			System.out.println("========================");
			System.out.println("----- MEAN SQUARES -----");
			System.out.println("========================");
			System.out.println("ms p: " + msParticipants);
			System.out.println("ms f1: " + msF1);
			System.out.println("ms f1xp: " + msF1xP);
			System.out.println("ms f2: " + msF2);
			System.out.println("ms f2xp: " + msF2xP);
			System.out.println("ms f1xf2: " + msF1xF2);
			System.out.println("ms f1xf2xp: " + msF1xF2xP);
			System.out.println("ms f3: " + msF3);
			System.out.println("ms f1xf3: " + msF1xF3);
			System.out.println("ms f1xp(f3): " + msF1xPxF3);
			System.out.println("ms f2xf3: " + msF2xF3);
			System.out.println("ms f2xp(f3): " + msF2xPxF3);
			System.out.println("ms p(f3): " + msPxF3);
			System.out.println("ms f1xf2xf3: " + msF1xF2xF3);
			System.out.println("ms f1xf2xp(f3): " + msF1xF2xPxF3);
			System.out.println("========================");
			System.out.println("----- F STATISTICS -----");
			System.out.println("========================");
			System.out.println("f f1: " + fF1);
			System.out.println("f f2: " + fF2);
			System.out.println("f f1xf2: " + fF1xF2);
			System.out.println("f f3: " + fF3);
			System.out.println("f f1xf3: " + fF1xF3);
			System.out.println("f f2xf3: " + fF2xF3);
			System.out.println("f f1xf2xf3: " + fF1xF2xF3);
			System.out.println("==============================");
			System.out.println("----- p FOR F STATISTICS -----");
			System.out.println("==============================");
			System.out.println("p f f1: " + pF1);
			System.out.println("p f f2: " + pF2);
			System.out.println("p f f1xf2: " + pF1xF2);
			System.out.println("p f f3: " + pF3);
			System.out.println("p f f1xf3: " + pF1xF3);
			System.out.println("p f f2xf3: " + pF2xF3);
			System.out.println("p f f1xf2xf3: " + pF1xF2xF3);
		}

		// -----------------------------------
		// -m option (output main effect means)
		// ------------------------------------

		if (outputMainEffectMeans)
		{
			System.out.println("=============================");
			System.out.println("----- MAIN EFFECT MEANS -----");
			System.out.println("=============================");
			System.out.println("Grand mean: " + grandMean);
			printEffectMeans(true, "Participant means: ", participantMeans, participantNames);
			printEffectMeans(validF1, f1Name + " means: ", levelMeansForF1, levelNamesForF1);
			printEffectMeans(validF2, f2Name + " means: ", levelMeansForF2, levelNamesForF2);
			printEffectMeans(validF3, f3Name + " means: ", levelMeansForF3, levelNamesForF3);
		}

		// ------------------------------
		// -a option (output ANOVA table)
		// ------------------------------

		if (outputAnovaTable)
		{
			printHeader(dvName, headerOption);
			if (!validF1 && !validF2 && validF3)
			{
				// one between-subjects factor
				printEffect(f3Name, dfF3, ssF3, msF3, fF3, pF3);
				printEffect("Residual", dfPxF3, ssPxF3, msPxF3, -1D, -1D);

			} else if (validF1 && !validF2 && !validF3)
			{
				// one within-subjects factor
				printEffect("Participant", dfP, ssParticipants, msParticipants, -1D, -1D);
				printEffect(f1Name, dfF1, ssF1, msF1, fF1, pF1);
				printEffect(f1Name + "_x_Par", dfF1xP, ssF1xP, msF1xP, -1D, -1D);

			} else if (validF1 && !validF2 && validF3)
			{
				// one within-subjects factor & one between-subjects factor
				printEffect(f3Name, dfF3, ssF3, msF3, fF3, pF3);
				printEffect("Participant(" + f3Name + ")", dfPxF3, ssPxF3, msPxF3, -1D, -1D);
				printEffect(f1Name, dfF1, ssF1, msF1, fF1, pF1);
				printEffect(f1Name + "_x_" + f3Name, dfF1xF3, ssF1xF3, msF1xF3, fF1xF3, pF1xF3);
				printEffect(f1Name + "_x_P(" + f3Name + ")", dfF1xPxF3, ssF1xPxF3, msF1xPxF3, -1D, -1D);

			} else if (validF1 && validF2 && !validF3)
			{
				// two within-subjects factors
				printEffect("Participant", dfP, ssParticipants, msParticipants, -1D, -1D);
				printEffect(f1Name, dfF1, ssF1, msF1, fF1, pF1);
				printEffect(f1Name + "_x_Par", dfF1xP, ssF1xP, msF1xP, -1D, -1D);
				printEffect(f2Name, dfF2, ssF2, msF2, fF2, pF2);
				printEffect(f2Name + "_x_Par", dfF2xP, ssF2xP, msF2xP, -1D, -1D);
				printEffect(f1Name + "_x_" + f2Name, dfF1xF2, ssF1xF2, msF1xF2, fF1xF2, pF1xF2);
				printEffect(f1Name + "_x_" + f2Name + "_x_Par", dfF1xF2xP, ssF1xF2xP, msF1xF2xP, -1D, -1D);

			} else if (validF1 && validF2 && validF3)
			{
				// two within-subjects factors & one between-subjects factor
				printEffect(f3Name, dfF3, ssF3, msF3, fF3, pF3);
				printEffect("Participant(" + f3Name + ")", dfPxF3, ssPxF3, msPxF3, -1D, -1D);
				printEffect(f1Name, dfF1, ssF1, msF1, fF1, pF1);
				printEffect(f1Name + "_x_" + f3Name, dfF1xF3, ssF1xF3, msF1xF3, fF1xF3, pF1xF3);
				printEffect(f1Name + "_x_P(" + f3Name + ")", dfF1xPxF3, ssF1xPxF3, msF1xPxF3, -1D, -1D);
				printEffect(f2Name, dfF2, ssF2, msF2, fF2, pF2);
				printEffect(f2Name + "_x_" + f3Name, dfF2xF3, ssF2xF3, msF2xF3, fF2xF3, pF2xF3);
				printEffect(f2Name + "_x_P(" + f3Name + ")", dfF2xPxF3, ssF2xPxF3, msF2xPxF3, -1D, -1D);
				printEffect(f1Name + "_x_" + f2Name, dfF1xF2, ssF1xF2, msF1xF2, fF1xF2, pF1xF2);
				printEffect(f1Name + "_x_" + f2Name + "_x_" + f3Name, dfF1xF2xF3, ssF1xF2xF3, msF1xF2xF3, fF1xF2xF3,
						pF1xF2xF3);
				printEffect(f1Name + "_x_" + f2Name + "_x_P(" + f3Name + ")", dfF1xF2xPxF3, ssF1xF2xPxF3, msF1xF2xPxF3,
						-1D, -1D);

			} else
			{
				System.out.println("Unknown design!");
			}
			printFooter();
		}

		// ------
		// done!
		// -----
	}

	// ---------------------------------
	// functions to generate ANOVA table
	// ---------------------------------

	static void printHeader(String dvName, boolean headerOption)
	{
		System.out.println();
		if (headerOption)
			System.out.printf("ANOVA_table_for_%s\n", dvName);
		else
			System.out.printf("ANOVA_table\n");
		System.out.printf("===========================================================================\n");
		System.out.printf("Effect                  df        SS            MS         F        p      \n");
		System.out.printf("---------------------------------------------------------------------------\n");
	}

	static void printFooter()
	{
		System.out.printf("===========================================================================\n");
	}

	// print a line in the ANOVA table
	static void printEffect(String effect, int df, double ss, double ms, double f, double p)
	{
		// limit the effect string to 22 characters
		if (effect.length() > 22)
			effect = effect.substring(0, 22);

		String fString = f != -1D ? String.format("%8.3f", f) : "";
		String pString = p != -1D ? String.format("%8.4f", p) : "";
		System.out.printf("%-22s %3d  %12.3f  %12.3f  %s  %s\n", effect, df, ss, ms, fString, pString);
	}

	static void printArray(String s, double ad[], String as[])
	{
		System.out.print(s + "= [");
		for (int i = 0; i < ad.length; i++)
		{
			System.out.print(as[i] + "=");
			if (ad[i] % 1.0D != 0.0D)
				System.out.printf("%.5f", ad[i]);
			else
				System.out.print(Math.round(ad[i]));
			if (i < ad.length - 1)
				System.out.print(", ");
		}
		System.out.println("]");
	}

	static void printEffectMeans(boolean flag, String effectName, double levelMeans[], String levelNames[])
	{
		if (!flag)
			return;
		if (!effectName.substring(0, 1).equals("."))
		{
			System.out.printf("%s\n", effectName);
			for (int i = 0; i < levelMeans.length; i++)
			{
				System.out.printf("   %s=", levelNames[i]);
				if (levelMeans[i] % 1.0D != 0.0D)
					System.out.printf("%.5f\n", levelMeans[i]);
				else
					System.out.printf("%d\n", Math.round(levelMeans[i]));
			}
		}
	}

	static void usage()
	{
		System.out.println("------------------------------------------------------------------------\n"
				+ "Usage: java Anova2 file p f1 f2 f3 [-a] [-d] [-m] [-h]\n" + "\n"
				+ "  file = data file (comma or space delimited)\n" + "  p = # of rows (participants) in data file\n"
				+ "  f1 = # of levels, 1st within-subjects factor (\".\" if not used)\n"
				+ "  f2 = # of levels, 2nd within-subjects factor (\".\" if not used)\n"
				+ "  f3 = # of levels, between-subjects factor (\".\" if not used)\n" + "  -a = output ANOVA table\n"
				+ "  -d = output debug data\n" + "  -m = output main effect means\n"
				+ "  -h = data file includes header lines (see API for details)\n" + "  (Note: default is no output)\n"
				+ "------------------------------------------------------------------------");
		System.exit(0);
	}

	static void headerError()
	{
		System.out.println("Header format error! Consult API for details.");
		System.exit(0);
	}
}
