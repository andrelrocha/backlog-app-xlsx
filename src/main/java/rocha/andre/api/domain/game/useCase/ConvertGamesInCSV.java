package rocha.andre.api.domain.game.useCase;

import org.springframework.stereotype.Component;
import rocha.andre.api.domain.game.DTO.GameDTO;
import rocha.andre.api.domain.game.DTO.GameReadCSVDTO;
import rocha.andre.api.domain.game.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertGamesInCSV {
    public List<Game> readGamesFromCSV() throws IOException {
        List<Game> games = new ArrayList<>();

        //lê o csv
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("csv/gamesbacklog.csv");
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))
        ) {

            String line;
            //pula linha cabeçário
            String headerLine = br.readLine();

            // Lê cada linha do arquivo CSV
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] data = line.split(",");

                for (String value : data) {
                    System.out.println(value);
                }

                var gameReadCSVDTO = new GameReadCSVDTO(data[0], data[1], data[2], data[3], data[4], data[5]);
                //converte para booleano
                boolean played = convertToBoolean(gameReadCSVDTO.played());

                // Converte para int os valores que deveriam ser inteiros
                int length = Integer.parseInt(gameReadCSVDTO.length());
                int metacritic = Integer.parseInt(gameReadCSVDTO.metacritic());
                int excitement = Integer.parseInt(gameReadCSVDTO.excitement());

                var gameDTO = new GameDTO(gameReadCSVDTO.name(), length, metacritic, excitement, gameReadCSVDTO.genre(), played);

                // Cria um novo objeto Game com os valores convertidos
                var game = new Game(gameDTO, played);
                games.add(game);

            }
        }

        return games;
    }

    private boolean convertToBoolean(String value) {
        return value != null && value.equalsIgnoreCase("true");
    }
}
