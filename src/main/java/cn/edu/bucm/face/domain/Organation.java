package cn.edu.bucm.face.domain;

public class Organation {
	 private String Orgid;
     private String name;;
private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Organation() {
		super();
	}

	public String getOrgid() {
		return Orgid;
	}

	public void setOrgid(String orgid) {
		Orgid = orgid;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Orgid == null) ? 0 : Orgid.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organation other = (Organation) obj;
		if (Orgid == null) {
			if (other.Orgid != null)
				return false;
		} else if (!Orgid.equals(other.Orgid))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Organation [Orgid=" + Orgid + ", name=" + name + "]";
	}

	
     
}
