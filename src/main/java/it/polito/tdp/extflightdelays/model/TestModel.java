package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		
		model.creaGrafo(200);
		System.out.println(model.getInfoGrafo());
		System.out.println(model.getArchi());

	}

}
