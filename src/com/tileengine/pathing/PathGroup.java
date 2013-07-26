package com.tileengine.pathing;

import java.util.ArrayList;

public class PathGroup 
{
    private ArrayList sections;
    private int repeatTotal;
    private int repeatCount;

    private int currentRun;
    private int currentSectionIndex;

    public PathGroup(ArrayList sections, int repeatTotal)
    {
        this.sections = sections;
        this.repeatTotal = repeatTotal;
        this.currentRun = 0;
        this.currentSectionIndex = 0;
        this.repeatCount = 0;

    }

    public int getRepeatCount()
    {
        return this.repeatCount;
    }

    public int getRepeatTotal() 
    {
        return this.repeatTotal;
    }

    public void addSection(PathSection section)
    {
        this.sections.add(section);
    }

    public void addSection(PathSection section, int index)
    {
        this.sections.add(index, section);
    }
    
    public void removeSection(PathSection section)
    {
        this.sections.remove(section);
    }
    
    public Object getCurrentSection()
    {
        return sections.get(currentSectionIndex);
    }

    public int currentPathIndex() 
    {
        return this.currentSectionIndex;
    }

    public boolean next()
    {
        boolean repeatLimit = false;

        if(this.currentSectionIndex < this.sections.size() - 1)
        {
            this.currentSectionIndex += 1;
        }
        else
        {
            this.repeatCount += 1;
            this.currentSectionIndex = 0;
        }

        if(this.repeatCount == this.repeatTotal)
        {
            repeatLimit = true;

            this.repeatCount = 0;
        }

        return repeatLimit;
    }
}
