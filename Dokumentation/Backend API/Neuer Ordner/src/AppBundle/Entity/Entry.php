<?php
/**
 * User: nsh
 * Date: 10.07.2017
 * Time: 11:36
 */

namespace AppBundle\Entity;


use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

/**
 * @ORM\Entity
 * @ORM\Table(name="entry")
 */
class Entry
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     * @ORM\Column(type="integer")
     */
    protected $id;
    /**
     * @var Activity
     *
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\Activity")
     * @ORM\JoinColumn(nullable=false, )
     * @Assert\NotNull()
     */
    protected $activity;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(type="datetime", name="start")
     * @Assert\NotNull()
     */
    protected $start;

    /**
     * @var \DateTime|null
     *
     * @ORM\Column(type="datetime", name="end")
     * @Assert\NotNull()
     * @Assert\Expression("this.getEnd() >= this.getStart()", message="entry.end.beforeStart")
     */
    protected $end;

    /**
     * @var string
     *
     * @ORM\Column(type="string", name="description", length=511)
     * @Assert\Length(min="3", max="511")
     */
    protected $description;

    /**
     * @var User|null
     *
     * @ORM\ManyToOne(targetEntity="AppBundle\Entity\User")
     * @ORM\JoinColumn(nullable=true, onDelete="CASCADE")
     * @Assert\NotNull()
     */
    protected $user;

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     * @return Entry
     */
    public function setId($id)
    {
        $this->id = $id;

        return $this;
    }

    /**
     * @return Activity|null
     */
    public function getActivity()
    {
        return $this->activity;
    }

    /**
     * @param Activity $activity
     * @return Entry
     */
    public function setActivity($activity)
    {
        $this->activity = $activity;

        return $this;
    }

    /**
     * @return \DateTime|null
     */
    public function getStart()
    {
        return $this->start;
    }

    /**
     * @param \DateTime|null $start
     * @return Entry
     */
    public function setStart($start)
    {
        $this->start = $start;

        return $this;
    }

    /**
     * @return \DateTime|null
     */
    public function getEnd()
    {
        return $this->end;
    }

    /**
     * @param \DateTime|null $end
     * @return Entry
     */
    public function setEnd($end)
    {
        $this->end = $end;

        return $this;
    }

    /**
     * @return string
     */
    public function getDescription()
    {
        return $this->description;
    }

    /**
     * @param string $description
     * @return Entry
     */
    public function setDescription($description)
    {
        $this->description = $description;

        return $this;
    }

    /**
     * @return User|null
     */
    public function getUser()
    {
        return $this->user;
    }

    /**
     * @param User|null $user
     * @return Entry
     */
    public function setUser($user)
    {
        $this->user = $user;

        return $this;
    }


}