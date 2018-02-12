package bimr.bfcl.dto;

public class MigrationDTO {

	private String migrationId;
	private String species;
	private HotspotDTO fromHotspot;
	private HotspotDTO toHotspot;

	public String getMigrationId() {
		return migrationId;
	}

	public void setMigrationId(String migrationId) {
		this.migrationId = migrationId;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public HotspotDTO getFromHotspot() {
		return fromHotspot;
	}

	public void setFromHotspot(HotspotDTO fromHotspot) {
		this.fromHotspot = fromHotspot;
	}

	public HotspotDTO getToHotspot() {
		return toHotspot;
	}

	public void setToHotspot(HotspotDTO toHotspot) {
		this.toHotspot = toHotspot;
	}
}
