// variables

ListView listView;
    List<GrupoListas> lista;




// metodos  debajo de on create

   public void setLista(){

        AdapterListas adapterListas = new AdapterListas(this, getData());
        listView.setAdapter(adapterListas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GrupoListas grupoListas = lista.get(position);
                Toast.makeText(getApplicationContext(), "Selecciono: "+ grupoListas.nombreLista, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<GrupoListas> getData() {

        lista = new ArrayList<>();

        // aqui agrego id, nombre, cantidad, progress
        lista.add(new GrupoListas(1, "Prueba 1", "5/10", 30));
        lista.add(new GrupoListas(2, "Prueba 2", "8/15", 50));
        lista.add(new GrupoListas(3, "Prueba 3", "2/12", 80));
        lista.add(new GrupoListas(4, "Prueba 4", "6/7", 10));

        return lista;
    }