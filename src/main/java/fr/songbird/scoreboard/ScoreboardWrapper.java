    private ChatColor getAdequateColor(String word)
    {
        String wordUC = word.toUpperCase();
        if(wordUC.contains("AQUA"))
        {
            colorPrefix = "AQUA";
            return ChatColor.AQUA;
        }
        else if(wordUC.contains("BLACK"))
        {
            colorPrefix = "BLACK";
            return ChatColor.BLACK;
        }
        else if(wordUC.contains("BLUE"))
        {
            colorPrefix = "BLUE";
            return ChatColor.BLUE;
        }
        else if(wordUC.contains("BOLD"))
        {
            colorPrefix = "BOLD";
            return ChatColor.BOLD;
        }
        else if(wordUC.contains("DARK_AQUA"))
        {
            colorPrefix = "DARK_AQUA";
            return ChatColor.DARK_AQUA;
        }
        else if(wordUC.contains("DARK_BLUE"))
        {
            colorPrefix = "DARK_BLUE";
            return ChatColor.DARK_BLUE;
        }
        else if(wordUC.contains("DARK_GRAY"))
        {
            colorPrefix = "DARK_GRAY";
            return ChatColor.DARK_GRAY;
        }
        else if(wordUC.contains("DARK_GREEN"))
        {
            colorPrefix = "DARK_GREEN";
            return ChatColor.DARK_GREEN;
        }
        else if(wordUC.contains("DARK_PURPLE"))
        {
            colorPrefix = "DARK_PURPLE";
            return ChatColor.DARK_PURPLE;
        }
        else if(wordUC.contains("DARK_RED"))
        {
            colorPrefix = "DARK_RED";
            return ChatColor.DARK_RED;
        }
        else if(wordUC.contains("GOLD"))
        {
            colorPrefix = "GOLD";
            return ChatColor.GOLD;
        }
        else if(wordUC.contains("GRAY"))
        {
            colorPrefix = "GRAY";
            return ChatColor.GRAY;

        }
        else if(wordUC.contains("GREEN"))
        {
            colorPrefix = "GREEN";
            return ChatColor.GREEN;
        }
        else if(wordUC.contains("ITALIC"))
        {
            colorPrefix = "ITALIC";
            return ChatColor.ITALIC;
        }
        else if(wordUC.contains("LIGHT_PURPLE"))
        {
            colorPrefix = "LIGHT_PURPLE";
            return ChatColor.LIGHT_PURPLE;
        }
        else if(wordUC.contains("MAGIC"))
        {
            colorPrefix = "MAGIC";
            return ChatColor.MAGIC;
        }
        else if(wordUC.contains("RED"))
        {
            colorPrefix = "RED";
            return ChatColor.RED;
        }
        else if(wordUC.contains("YELLOW"))
        {
            colorPrefix = "YELLOW";
            return ChatColor.YELLOW;
        }

        return null;
    }

    /**
     * Initializes keywords.
     * @see {@link ScoreboardWrapper#tk}
     * @see {@link ScoreboardWrapper#lk}
     */
    private void initializeKeywords()
    {
        try
        {
            tk = new TimerKeyword("timer");
            lk = new LocationKeyword("location");
        }catch(Exception e0)
        {
            e0.printStackTrace();
        }
    }


    /**
     * Loads scoreboard configuration file in the memory
     * @see {@link ScoreboardWrapper#scoreboardConfig}
     */
    private void loadScoreboardConfigFile()
    {
        try
        {
            scoreboardConfig = ConfigYamlFile.getYamlFile(scoreboardConfigFile);
        }catch(DataIntegrityException die0)
        {
            LOGGER.error(die0.getMessage());
        }
    }

    /**
     * Example:<br>
     * <code>
     *     new ScoreboardWrapper().formatMe(2.5); // => 2:30:0 (2 hours:30 minutes:0 seconds
     * </code>
     * @param hour Can be a Double|Integer object
     * @return formatted hour
     * @throws Exception
     */
    public String formatMe(Number hour) throws Exception, AssertionError
    {
        assert (hour instanceof Double || hour instanceof Integer) : "Must be Double|Integer instance. hour="+hour;
        if((double)hour > 0.0)
        {
            int base = (int)hour * 3600;
            int base_minute = base/60;
            int seconds = base%60;
            int hours = base_minute/60;
            int minutes = base_minute%60;

            return Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds);
        }

        throw new Exception("hour =< 0");
    }

    public Scoreboard getCurrentScoreboard()
    {
        return scoreboard;
    }

}
