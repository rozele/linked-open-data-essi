package org.agu.essi;

public enum MeetingType 
{
	FALL 
	{
		public String toString()
		{
			return "FM";
		}
	},
	JOINT
	{
		public String toString()
		{
			return "JM";
		}
	}, 
	SPRING 
	{
		public String toString()
		{
			return "SM";
		}
	}, 
	AMERICAS
	{
		public String toString()
		{
			return "MA";
		}
	}
}
