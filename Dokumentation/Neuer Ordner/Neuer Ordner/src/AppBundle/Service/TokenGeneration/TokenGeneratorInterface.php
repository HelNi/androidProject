<?php
/**
 * Created by PhpStorm.
 * User: Admin
 * Date: 09.07.2017
 * Time: 18:09
 */

namespace AppBundle\Service\TokenGeneration;


use AppBundle\Entity\User;

interface TokenGeneratorInterface
{
    /**
     * @param User $user
     * @return string
     */
    public function generateToken(User $user);
}