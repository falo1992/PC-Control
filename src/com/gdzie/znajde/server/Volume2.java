package com.gdzie.znajde.server;

import javax.sound.sampled.*;

public class Volume2 {

	private static void printControl(Control control, String indent)
	{
	    System.out.printf("%s%s%n", indent, control);
	    if (control instanceof CompoundControl)
	    {
	        Control[] controls = ((CompoundControl)control).getMemberControls();
	        indent += "  ";
	        for (Control c: controls)
	        {
	            printControl(c, indent);
	        }
	    }
	}
	public static void main(String[] args) {
		Mixer.Info [] mixers = AudioSystem.getMixerInfo();
		System.out.println("There are " + mixers.length + " mixer info objects");
		for (Mixer.Info mixerInfo : mixers)
		{
			//if(mixerInfo.getDescription().contains("Playback"))
		    System.out.println("mixer name: " + mixerInfo.getName() + mixerInfo.getDescription());
		    Mixer mixer = AudioSystem.getMixer(mixerInfo);
		    
		    try {
				mixer.open();
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    Line.Info [] lineInfos = mixer.getTargetLineInfo(); // target, not source
		    for (Line.Info lineInfo : lineInfos)
		    {
		        System.out.println("  Line.Info: " + lineInfo);
		        Line line = null;
		        boolean opened = true;
		        try
		        {
		            line = mixer.getLine(lineInfo);
		            opened = line.isOpen(); //|| line instanceof javax.sound.sampled.;
		            if (!opened)
		            {
		                line.open();
		            }
		            Control[] controls = line.getControls();
		            for (Control control: controls)
		            {
		                printControl(control, "    ");
		            }
		        }
		        catch (LineUnavailableException e)
		        {
		            e.printStackTrace();
		        }
		        catch (IllegalArgumentException iaEx)
		        {
		            System.out.println("    " + iaEx);
		        }
		        finally
		        {
		            if (line != null && !opened)
		            {
		                line.close();
		            }
		        }
		    }
		}

	}
}
