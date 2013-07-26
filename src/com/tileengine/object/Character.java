package com.tileengine.object;

import com.tileengine.dialog.Dialog;
import com.tileengine.battle.attack.Attack;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Character Class
 *
 * @author James Keir
 */
public class Character extends MoveableObject
{
    /**
     * Characters Level
     */
    private int level = 1;

    /**
     * Characters Hit Points
     */
    private int baseHitPoints = 0;

    /**
     * Characters Mana Pool
     */
    private int baseManaPool = 0;

    /**
     * Characters Current Hit Points
     */
    private int currentHitPoints = 0;

    /**
     * Characters Current Mana Pool
     */
    private int currentManaPool = 0;


    /**
     * Used to store characters spells
     */
    private ArrayList magicSpells;

    /**
     * Used to Calculate Hit Points
     */
    private int stamina = 10;
    
    /**
     * Used to Calculate Mana Pool
     */
    private int intelligence = 10;
    
    /**
     * Used to calculate damage for a physical attack
     */
    private int strength = 10;

    /**
     * Used to calcualate damage for magic attacks
     */
    private int spellPower = 10;

    /**
     * Used to calcualte hits/misses
     */
    private int dexterity = 10;

    /**
     * Used to calculate is a someone will hit/miss
     */
    private int agility = 10;

    /**
     * Used to midigate physical damage
     */
    private int armorClass = 10;

    /**
     * Used to midigate magical damage
     */
    private int magicResilience = 10;

    /**
     * Used to adjust battle speed
     */
    private int battleSpeed = 3000;

    /**
     * Battle speed current counter
     */
    private int nextAttackCounter = 0;

    /*
     * Dialog Object
     */
    private Dialog dialogObject = null;

    /**
     * Create a Dialog Character thats not a sprite
     *
     * @param id ID of sprite
     * @param image image of object
     * @param locationX Location x of object
     * @param locationY Location Y of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     * @param isPushable Of the object is pushable or not
     * @param movementSpeed Movement speed of object
     */
    public Character(String id, Image image, int locationX, int locationY, int width, int height, boolean isPenetrable, double movementSpeed)
    {
        super(id, image, locationX, locationY, width, height, isPenetrable, movementSpeed);
    }

    /**
     * Create a Dialog Character thats is a sprite
     * 
     * @param id ID of sprite
     * @param spriteImages Sprite Images
     * @param locationX Location x of object
     * @param locationY Location Y of object
     * @param width Width of object
     * @param height Height of object
     * @param isPenetrable If the object is penetrable or not
     * @param isPushable Of the object is pushable or not
     * @param movementSpeed Movement speed of object
     * @param loopsPerSecond How many times to run the animation per second
     * @param spriteType The type of sprite
     */
    public Character(String id, Image[][] spriteImages, int locationX, int locationY, int width, int height, boolean isPenetrable, double movementSpeed, int loopsPerSecond, int spriteType)
    {
        super(id, spriteImages, locationX, locationY, width, height, isPenetrable, movementSpeed, loopsPerSecond, spriteType);
    }

    /**
     * Sets characters level
     * @param level Level to set character to
     */
    public void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * Gets the characters level
     * @return Returns characters level
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Set characters base hit points
     * @param baseHitPoints Characters base hit points
     */
    public void setBaseHitPoints(int baseHitPoints)
    {
        this.baseHitPoints = baseHitPoints;
    }

    /**
     * Get characters base hit points
     * @return current base hit Points
     */
    public int getBaseHitPoints()
    {
        return baseHitPoints;
    }

    /**
     * Get characters base mana pool
     * @param baseManaPool Characters base mana pool
     */
    public void setBaseManaPool(int baseManaPool)
    {
        this.baseManaPool = baseManaPool;
    }

    /**
     * Gets characters base mana pool
     * @return Returns characters base mana pool
     */
    public int getBaseManaPool()
    {
        return baseManaPool;
    }

    /**
     * Set characters current hit points
     * @param baseHitPoints Characters current hit points
     */
    public void setCurrentHitPoints(int currentHitPoints)
    {
        this.currentHitPoints = currentHitPoints;
    }

    /**
     * Get characters current hit points
     * @return current current hit Points
     */
    public int getCurrentHitPoints()
    {
        return currentHitPoints;
    }

    /**
     * Get characters current mana pool
     * @param baseManaPool Characters current mana pool
     */
    public void setCurrentManaPool(int currentManaPool)
    {
        this.currentManaPool = currentManaPool;
    }

    /**
     * Gets characters current mana pool
     * @return Returns characters current mana pool
     */
    public int getCurrentManaPool()
    {
        return currentManaPool;
    }

    /**
     * Adds a spell to the character
     * @param magicSpell Spell to ass to character
     */
    public void addMagicSpell(Attack magicSpell)
    {
        if(magicSpells == null)
        {
            magicSpells = new ArrayList();
        }

        magicSpells.add(magicSpell);
    }

    /**
     * Removes a magic spell from the character
     * @param magicSpell Magic spell to remove from character
     */
    public void removeMagicSpell(Attack magicSpell)
    {
        if(magicSpells != null && magicSpells.contains(magicSpell))
        {
            magicSpells.remove(magicSpell);
        }
    }

    /**
     * Gets all the characters spells
     * @return Returns all the characters spells
     */
    public Attack[] getMagicSpells()
    {
        Attack[] chractersSpells = null;

        if(magicSpells != null)
        {
            magicSpells.toArray(chractersSpells);
        }

        return chractersSpells;
    }

    /**
     * Get a spell from the character
     * @param index The index of the spell
     * @return The spell
     */
    public Attack getMagicSpell(int index)
    {
        return (Attack) magicSpells.get(index);
    }

    /**
     * Set the stamina for the character which will increase/decrease their Hit Points
     * @param stamina Amount of stamina
     */
    public void setStaminia(int stamina)
    {
        this.stamina = stamina;
    }

    /**
     * Gets characters stamins
     * @return Character stamina
     */
    public int getStaminia()
    {
        return stamina;
    }

    /**
     * Set the intelligence for the character which will increase/decrease their Mana Pool
     * @param intelligence
     */
    public void setIntelligence(int intelligence)
    {
        this.intelligence = intelligence;
    }

    /**
     * Gets characters intelligence
     * @return Character intelligence
     */
    public int getIntelligence()
    {
        return intelligence;
    }

    /**
     * Set the strength for the character which will increase/decrease their Base Physical Damage
     * @param strength
     */
    public void setStrength(int strength)
    {
        this.strength = strength;
    }

    /**
     * Gets characters strength
     * @return Character strength
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Set the spell power for the character which will increase/decrease their Base Magical Damage
     * @param spellPower
     */
    public void setSpellPower(int spellPower)
    {
        this.spellPower = spellPower;
    }

    /**
     * Gets characters spell power
     * @return Character spell power
     */
    public int getSpellPower()
    {
        return spellPower;
    }

    /**
     * Set the dexterity for the character which will increase/decrease their chance to hit an enemy
     * @param dexterity
     */
    public void setDexterity(int dexterity)
    {
        this.dexterity = dexterity;
    }

    /**
     * Gets characters dexterity
     * @return Character dexterity
     */
    public int getDexterity()
    {
        return dexterity;
    }

    /**
     * Set the agility for the character which will increase/decrease their chance dodge an attack
     * @param agility
     */
    public void setAgility(int agility)
    {
        this.agility = agility;
    }

    /**
     * Gets characters agility
     * @return Character agility
     */
    public int getAgility()
    {
        return agility;
    }

    /**
     * Set the armor class for the character which will increase/decrease the amount of physical damage they take
     * @param armorClass
     */
    public void setArmorClass(int armorClass)
    {
        this.armorClass = armorClass;
    }

    /**
     * Gets characters armor class
     * @return Character armor class
     */
    public int getArmorClass()
    {
        return armorClass;
    }

    /**
     * Set the magic resilience for the character which will increase/decrease the amount of magical damage they take
     * @param magicResilience
     */
    public void setMagicResilience(int magicResilience)
    {
        this.magicResilience = magicResilience;
    }

    /**
     * Gets characters magic resilience
     * @return Character magic resilience
     */
    public int getMagicResilience()
    {
        return magicResilience;
    }

    /**
     * Sets dialog for Character
     *
     * @param dialogObject Dialog for character
     */
    public void setDialog(Dialog dialogObject)
    {
        this.dialogObject = dialogObject;
    }

    /**
     * Sets how often this character can attack
     * @param battleSpeed The number of milliseconds per attack
     */
    public void setBattleSpeed(int battleSpeed)
    {
        this.battleSpeed = battleSpeed;
    }

    /**
     * Returns the characters battle speed
     * @return The number of milliseconds between attacks
     */
    public int getBattleSpeed()
    {
        return battleSpeed;
    }

    /**
     * Adds time elapsed to attack counter
     * @param timeElapsed Amount of time to add
     */
    public void addNextAttack(int timeElapsed)
    {
        //Check to see if we need to add time
        if(nextAttackCounter < getBattleSpeed())
        {
            //Add Time
            nextAttackCounter += timeElapsed;

            //If Greater the battle speed set to battle speed
            if(nextAttackCounter > getBattleSpeed())
            {
                nextAttackCounter = getBattleSpeed();
            }
        }
    }

    /**
     * Gets current attack counter
     * @return Current attack counter
     */
    public int getNextAttack()
    {
        return nextAttackCounter;
    }

    /**
     * Resets attack counter
     */
    public void resetNextAttack()
    {
        nextAttackCounter = 0;
    }

    /**
     * Gets dialog for character
     *
     * @return Returns characters Dialog
     */
    public Dialog getDialog()
    {
        return dialogObject;
    }

    /**
     * Return is the Character has Dialog or Not
     * @return True/False
     */
    public boolean hasDialog()
    {
        return (dialogObject != null);
    }
}
