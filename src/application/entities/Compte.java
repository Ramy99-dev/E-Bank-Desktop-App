package application.entities;

public class Compte {
	private Long id ;
	private String propiertaire ;
	private Double solde ;
	private Double maxS = (double) 1000;
	private Double minS = (double) 800 ;
	private Double decouvert ; 
	public Compte(Long id , Double solde ,  String propiertaire)
	{
		this.id = id;
		this.solde = solde;
		this.propiertaire = propiertaire;
	}
	public Compte(Long id, String propiertaire, Double solde , Double minS , Double maxS , Double decouvert) {
		super();
		this.id = id;
		this.propiertaire = propiertaire;
		this.solde = solde;
		this.minS = minS ;
		this.maxS = maxS;
		this.decouvert = decouvert ; 
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPropiertaire() {
		return propiertaire;
	}
	public void setPropiertaire(String propiertaire) {
		this.propiertaire = propiertaire;
	}
	public Double getSolde() {
		return solde;
	}
	public void setSolde(Double solde) {
		this.solde = solde;
	}
	
	public Double getMaxS() {
		return maxS;
	}
	public void setMaxS(Double maxS) {
		this.maxS = maxS;
	}
	public Double getMinS() {
		return minS;
	}
	public void setMinS(Double minS) {
		this.minS = minS;
	}
	@Override
	public String toString() {
		return "id=" + id + ", propiertaire=" + propiertaire + ", solde=" + solde ;
	}
	public Double getDecouvert() {
		return decouvert;
	}
	public void setDecouvert(Double decouvert) {
		this.decouvert = decouvert;
	}
	

}
