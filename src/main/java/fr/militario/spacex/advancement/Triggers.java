package fr.militario.spacex.advancement;

public class Triggers {
	public static final CustomTrigger TAKEASEAT = new CustomTrigger("takeaseat");
	public static final CustomTrigger ONESMALLSTEPFORMAN = new CustomTrigger("onesmallstepforman");
	public static final CustomTrigger ONEGIANTLEAPFORMANKIND = new CustomTrigger("onegiantleapformankind");
	public static final CustomTrigger IGNITED = new CustomTrigger("ignited");
	public static final CustomTrigger RECYCLING = new CustomTrigger("recycling");
	public static final CustomTrigger[] TRIGGER_ARRAY = new CustomTrigger[] { TAKEASEAT, ONESMALLSTEPFORMAN, ONEGIANTLEAPFORMANKIND, IGNITED, RECYCLING };
}
