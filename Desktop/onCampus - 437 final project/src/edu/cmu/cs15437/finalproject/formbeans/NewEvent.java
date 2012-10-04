package edu.cmu.cs15437.hw4.formbeans;

import org.mybeans.form.FormBean;
import java.util.ArrayList;
import java.util.List;


public class NewEvent extends FormBean {
	private String event;
	private String caption;
	private int sday;
	private int smonth;
	private int syear;
	private int shour;
	private int sminute;
	private String sevening;
	private int eday;
	private int emonth;
	private int eyear;
	private int ehour;
	private int eminute;
	private String eevening;
	private String userId;	
	
	public String       getEvent()         { return event;         }
	public String       getCaption()        { return caption;        }
	public int       getSDay()        { return sday;        }
	public int 		getSMonth()	{ return smonth;        }
	public int 		getSYear()	{ return syear;        }
	public int 		getSHour()	{ return shour;        }
	public int 		getSMinute()	{ return sminute;        }
	public String 		getSEvening()	{ return sevening;        }
	public int       getEDay()        { return eday;        }
	public int 		getEMonth()	{ return emonth;        }
	public int 		getEYear()	{ return eyear;        }
	public int 		getEHour()	{ return ehour;        }
	public int 		getEMinute()	{ return eminute;        }
	public String 		getEEvening()	{ return eevening;        }
	public String 		getUserId()	{ return userId;        }

	public void setEvent(String s)         { event      = s;        }
	public void setCaption(String s)        { caption     = trimAndConvert(s,"<>\""); }
	public void setUserId(String s)         { userId      = s;        }
	public void setSDay(int i)				{ sday = i;			}
	public void setSMonth(int i)				{ smonth = i;		}
	public void setSYear(int i)				{ syear = i;		}
	public void setSHour(int i)				{ shour = i;		}
	public void setSMinute(int i)				{ sminute = i;		}
	public void setSEvening(String s)         { sevening      = s;        }
	public void setEDay(int i)				{ eday = i;			}
	public void setEMonth(int i)				{ emonth = i;		}
	public void setEYear(int i)				{ eyear = i;		}
	public void setEHour(int i)				{ ehour = i;		}
	public void setEMinute(int i)				{ eminute = i;		}
	public void setEEvening(String s)         { eevening      = s;        }
	
	
	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (event == null || event.length() == 0) {
			errors.add("Error: Event name is required");
		}

		if (caption == null || caption.length() == 0) {
			errors.add("Error: Caption is required");
		}
		
		return errors;
	}
}
